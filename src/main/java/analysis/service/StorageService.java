/*
package analysis.service;

import net.harawata.appdirs.AppDirsFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class StorageService {

    Path path;

    public StorageService(String filename) {
        // where do I store persistent data... based on OS-specific appdata dir and /MassTransitSimulator, e.g.
        // ~/.MassTransitSimulator/ (Linux) or
        // ~/Library/Application\ Support/MassTransitSimulator (macOS) or
        // somewhere else I don't remember on Windows
        String appDataPath = AppDirsFactory.getInstance().getSiteDataDir("MassTransitSimulator", null, null);
        if (!new File(appDataPath).exists()) {
            new File(appDataPath).mkdirs();
        }
        path = Paths.get(appDataPath, filename);
    }

    public String[][] readData() throws java.io.IOException {
        ArrayList<ArrayList<String>> datas = new ArrayList<>();
        Scanner in = new Scanner(path);
        while (in.hasNextLine()) {
            ArrayList<String> data = new ArrayList<>();
            for (String s: in.nextLine().split(",")) {
                data.add(s);
            }
            datas.add(data);
        }
        String[][] ret = new String[datas.size()][];
        for (int i = 0; i < ret.length; i++) {
            ArrayList<String> data = datas.get(i);
            ret[i] = ((List<String>)data).toArray(new String[data.size()]);
        }
        return ret;
    }

    public void saveData(String[][] datas) throws java.io.IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(path.toString()));
        for (String[] data: datas) {
            pw.write(String.join(",", data));
        }
        pw.close();
    }

}
*/
