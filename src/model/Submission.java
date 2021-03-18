package model;

import java.nio.file.Path;

public class Submission {
    private Path file;
    private final String name;
    private final String familyName;
    private final String mtrklNr;
    private final String title;

    public Submission(Path file, String name, String familyName, String mtrklNr, String title) {
        this.file = file;
        this.name = name;
        this.familyName = familyName;
        this.mtrklNr = mtrklNr;
        this.title = title;
    }

    public static Submission apply(Path path){
        String[] filename = path.toFile().getName().split("_");
        if (filename.length < 4) return null;
        String name = filename[0].split(" ")[0];
        String familyName = filename[0].split(" ")[1];
        String mtrklNr = filename[1];
        StringBuilder sb = new StringBuilder();
        for (int i = 4; i < filename.length; i++) {
            sb.append(filename[i]);
            sb.append(" ");
        }
        return new Submission(path,name,familyName,mtrklNr, sb.toString());
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public Path getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getMtrklNr() {
        return mtrklNr;
    }

    public String getTitle() {
        return title;
    }
}
