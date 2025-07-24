package Cafe.CafeOrderSystem.JsonParser;

import java.io.File;
import java.time.LocalTime;
import java.util.*;

public class JsonCollection<T> {
    private final ItemsParser parser;
    private final Class<T> type;
    private final File folderLoc;
    private final File[] folderFiles;
    private final List<T> collectionList;

    /**
     * This class is responsible for creating and managing a collection of Json Objects
     * It is generic in order to allow the various objects from the cafe system
     * @param fileParser The relevant parser for the object
     * @param folderPath The folder from which the collection fills and writes to
     */
    public JsonCollection (ItemsParser fileParser, String folderPath, Class<T> type) {
        this.parser = fileParser;
        this.type = type;
        this.folderLoc = getFolderLocation(folderPath);
        this.folderFiles = getFolderFiles();
        this.collectionList = new ArrayList<>();
    }

    /**
     * This Function Converts all files from the folderLoc folder and transforms them
     * from json files into the object type of the parser
     */
    public void startCollection (){
        if(this.collectionList.isEmpty()){
            for(File curFile : folderFiles){
                collectionList.addAll(parser.readFile(curFile, type));
            }
        }
    }

    /**
     * This Function Converts all the objects from the collection array list and writes them
     * into json files in the folderLoc file location
     */
    public void endCollection (){
        if(!this.collectionList.isEmpty()){
            File file = new File(generateFileName());
            parser.writeFile(file, collectionList);
        }
    }

    /**
     * Default Function to create filenames, Overrided by the collection class
     * for each object
     * @return returns a string which can be used as a filename
     */
    public String generateFileName (){
        return String.format("%s/%s-%s.json",
                folderLoc.getAbsolutePath(), type.getName(), LocalTime.now());
    }

    /**
     * This method allows object to be added to the collectionList
     * it forces the correct object with its parameter
     * it can be overriden in order to further input validation
     * @param newObject the object being added to the arraylist
     */
    public void addObject(T newObject){
        this.collectionList.add(newObject);
    }

    /**
     * Function used for finding a specific element at the entered Index
     * @param index the index which the desired object is at
     * @return desired object
     */
    public T getObject(int index){
        T tempObject = null;
        tempObject = this.collectionList.get(index);

        return tempObject;
    }

    /**
     * Removes Object at entered Index
     * @param index removes object at specified index
     */
    public void removeObject(int index){
        this.collectionList.remove(index);
    }

    /**
     * Loops through the elements of the list to see if queryObject exists in
     * the collection returning its index or -1 if it doesn't exist
     * @param queryObject
     * @return
     */
    public int findObject(T queryObject){
        for (int i = 0; i < collectionList.size(); i++){ //loops through all objects in the collection
            if(getObject(i).equals(queryObject)){ //Returns the index of a matching Object
                return i;
            }
        }
        return -1;
    }

    public List<T> getCollection(){
        return this.collectionList;
    }

    private File getFolderLocation(String folderPath){
        File folder = new File(folderPath);
        if(!folder.exists()){
            throw new IllegalArgumentException(
                    String.format("Folder %s does not exist", folderPath)
                    );
        }

        if(!folder.isDirectory()){
            throw new IllegalArgumentException(
                    String.format("Folder %s is not a directory", folderPath)
            );
        }

        return folder;
    }

    private File[] getFolderFiles(){
        return folderLoc.listFiles((dir, name) -> name.endsWith(".json"));
    }
}
