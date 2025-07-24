package Cafe.CafeOrderSystem.JsonParser;

import java.io.*;
import java.util.*;

public interface CafeObjectParser<T> {
    List<T> getItems(File jsonFile);
}
