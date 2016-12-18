package com.guncy.android.cardreader;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.guncy.android.cardreader.lib.FeliCa;
import com.guncy.android.cardreader.model.Card;

/**
 * Created by dongri on 2016/12/18.
 */

public class MainActivity extends AppCompatActivity {

    private CardListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_listview);

        adapter = new CardListAdapter(getApplicationContext());

        int padding = (int) (getResources().getDisplayMetrics().density * 8);
        ListView listView = (ListView) findViewById(R.id.card_list);
        listView.setPadding(padding, 0, padding, 0);
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        listView.setDivider(null);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View header = inflater.inflate(R.layout.list_header_footer, listView, false);
        View footer = inflater.inflate(R.layout.list_header_footer, listView, false);
        listView.addHeaderView(header, null, false);
        listView.addFooterView(footer, null, false);
        listView.setAdapter(adapter);

        // NFC(FeliCa) ID を取得
        byte[] felicaIDm;
        Intent intent = getIntent();
        Tag nfcTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(nfcTag != null) {
            felicaIDm = nfcTag.getId();
        }else {
            Toast.makeText(this, "カードを後ろにかざしてください", Toast.LENGTH_LONG).show();
            return;
        }

        NfcF felica = NfcF.get(nfcTag);

        try {
            felica.connect();
            byte[] req = FeliCa.readWithoutEncryption(felicaIDm, 10);
            byte[] res = felica.transceive(req);
            felica.close();
            parsePasmoHistory(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parsePasmoHistory(byte[] res) throws Exception {
        // res[0] = データ長
        // res[1] = 0x07
        // res[2〜9] = カードID
        // res[10,11] = エラーコード。0=正常。
        if (res[10] != 0x00) {
            throw new RuntimeException("Felica error.");
        }

        // res[12] = 応答ブロック数
        // res[13+n*16] = 履歴データ。16byte/ブロックの繰り返し。
        int size = res[12];
        int payment = 0;
        for (int i = 0; i < size; i++) {
            FeliCa felica = FeliCa.parse(res, 13 + i * 16);
            Card card = Card.getCard(getBaseContext(), felica);
            if (i < size-1) {
                FeliCa nextFelica = FeliCa.parse(res, 13 + (i+1) * 16);
                Card nextCard = Card.getCard(getBaseContext(), nextFelica);
                payment = Integer.parseInt(card.getBalance()) - Integer.parseInt(nextCard.getBalance());
            }
            card.setPayment(String.valueOf(payment));
            adapter.add(card);
        }
    }
}