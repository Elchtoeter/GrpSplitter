package model;

import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Model {

    private File folder;
    private List<Path> files;
    private List<Integer> chosenGroups;
    private int entries = 0;
    private final List<List<Path>> grps = new ArrayList<>();
    private List<Submission> subs;
    List<List<Path>> chosenSubs = new ArrayList<>();

    public void setFolder(File folder) {
        this.folder = folder;
        loadFiles();
    }

    private void loadFiles() {
        try (Stream<Path> paths = Files.walk(folder.toPath())) {
            files = paths.filter(Files::isRegularFile).filter(i -> i.getFileName().toString().endsWith("zip")).collect(Collectors.toList());
            entries = files.size();
            files.sort(Comparator.naturalOrder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int chunkSize = entries/6;
        int rest = entries%6;
        for (int i = 0; i < rest; i++) {
            int listStart = i * (chunkSize+1);
            int listEnd = (chunkSize+1) + (i * (chunkSize+1));
            grps.add(i, files.subList(listStart,listEnd));
        }
        for (int i = 0; i < 6 - rest; i++) {
            int listStart = ((chunkSize+1)*rest) + (i*chunkSize);
            int listEnd = chunkSize + (((chunkSize+1)*rest) + (i*chunkSize));
            grps.add((i+rest),files.subList(listStart,listEnd));
        }
    }

    public List<List<Path>> getFiles() {
        return grps;
    }

    public int getEntries() {
        return entries;
    }

    public void setChosenGroups(List<Integer> chosenGroups) {
        this.chosenGroups = chosenGroups;
    }

    public boolean extractAndMove() {
        for (Integer chosenGroup : chosenGroups) {
            chosenSubs.add(grps.get(chosenGroup));
        }
        List<Submission> cs = chosenSubs.stream().flatMap(List::stream).map(Submission::apply).collect(Collectors.toList());
        for (Submission c : cs) {
            File old = c.getFile().toFile();
            String newFilename = c.getName() + " " + c.getFamilyName() + " k" + c.getMtrklNr() +".zip";
            File newF = new File(newFilename);
            old.renameTo(newF);
        }
        return false;
    }
}
