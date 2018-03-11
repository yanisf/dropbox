package server.core;

import java.io.File;

public class FileManager {
    private final static String DIRPATH = "D://DropBoxDatabase";

    //создаём директорию
    public void createDir(String userId) {
        StringBuilder path = new StringBuilder();
        path.append(DIRPATH).append("//").append("user_").append(userId);
        File dir = new File(path.toString());
        boolean result = dir.mkdir();
        path.insert(0, "Directory ");
        System.out.println(result ? path.append(" сreated").toString() : path.append(" was not сreated").toString());
    }
}
