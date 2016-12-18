package com.guncy.android.cardreader.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dongri on 2016/12/18.
 */

public class Station {
    public String lineName;
    public String stationName;

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }


    public static Station getStation(Context context, int lineCode, int stationCode) {
        try {
            InputStream is = context.getAssets().open("StationCode.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] token = line.split(",", -1);
                if ((token[1].equals(String.valueOf(lineCode))) && (token[2].equals(String.valueOf(stationCode)))){
                    Station station = new Station();
                    station.setLineName(token[4]);
                    station.setStationName(token[5]);
                    return station;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Station();
        }
        return new Station();
    }

}
