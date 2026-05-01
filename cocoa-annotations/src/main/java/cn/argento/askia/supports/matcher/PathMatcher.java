package cn.argento.askia.supports.matcher;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface PathMatcher extends java.nio.file.PathMatcher {

    boolean matches(String input);

    @Override
    default boolean matches(Path path){
        return matches(path.toAbsolutePath().normalize().toString());
    }

    default List<String> filter(List<String> inputs){
        if (inputs == null){
            return null;
        }
        if (inputs.size() == 0){
            return new ArrayList<>();
        }
        List<String> matchesList = new ArrayList<>();
        for (String input : inputs){
            if (matches(input)){
                matchesList.add(input);
            }
        }
        return matchesList;
    }
}
