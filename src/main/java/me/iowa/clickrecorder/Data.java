package me.iowa.clickrecorder;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {

    @Getter
    @Setter
    private boolean testInProgress = false;

    private final List<Integer> downDelays = new ArrayList<>();
    @Getter
    private final List <Integer> upDelays = new ArrayList<>();
    private final List <Integer> clickDelays = new ArrayList<>();

    private long lastDownTime = -1;
    private long lastUpTime = -1;

    private File lastSavedTest;
    private String lastSavedString;

    public void recordDownTime(long time) {
        if (this.lastDownTime == -1) {
            this.lastDownTime = time;
        } else if (this.lastUpTime != -1) {
            downDelays.add((int) (time - lastUpTime));
            clickDelays.add((int) (time - lastDownTime));
            this.lastDownTime = time;
        }
    }

    public void recordUpTime(long time) {
        if (this.lastUpTime == -1) {
            this.lastUpTime = time;
        } else if (this.lastDownTime != -1) {
            upDelays.add((int) (time - lastDownTime));
            this.lastUpTime = time;

            // debug
            /*if (upDelays.size() % 10 == 0) {
                System.out.println("Up Delays: " + upDelays);
                System.out.println("Down Delays: " + downDelays);
                System.out.println("Click Delays: " + clickDelays);
            }*/
        }
    }

    public void resetData() {
        this.lastDownTime = -1;
        this.lastUpTime = -1;
        downDelays.clear();
        upDelays.clear();
        clickDelays.clear();
    }

    public void exportData(File saveLocation) {
        List<List<Integer>> clickLists = Arrays.asList(downDelays, upDelays, clickDelays);
        System.out.println(downDelays.size());
        System.out.println(upDelays.size());
        System.out.println(clickDelays.size());
        try {
            File dataFile = saveLocation == null ? new File(System.getProperty("java.io.tmpdir") + "data.csv") : saveLocation;
            FileWriter csvWriter = new FileWriter(dataFile);
            StringBuilder builder = new StringBuilder();

            for (List<Integer> list : clickLists) {
                for (Integer integer : list) {
                    builder.append(integer + ",");
                }
                builder.append("\n");
            }
            String data = builder.toString();

            // save to clipboard
            StringSelection selection = new StringSelection(data);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

            this.lastSavedString = data;

            // save to file
            csvWriter.append(data);
            csvWriter.flush();
            csvWriter.close();

            this.lastSavedTest = dataFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
