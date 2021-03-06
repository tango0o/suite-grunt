package com.tango.auto.utils;

import java.io.InputStream;
import java.util.*;

public class BaseHelper {

    private static Map<String, String> configKeyValueMap = null;
    private static Map<String, List<String>> suiteNodeClassNameMap = null;

    public BaseHelper() {
        configKeyValueMap = loadConfiguration("config.properties", Constants.Base_Dir_Path, Constants.Beigin_Folder_Name,
                Constants.Max_Folder_Levels, Constants.Extract_File_Path, Constants.Output_Xml_Path, Constants.Filte_Result_Status,
                Constants.TestNG_Listener, Constants.Is_Parallel, Constants.Is_Single_Instance);
        suiteNodeClassNameMap = new HashMap<String, List<String>>();
    }

    public BaseHelper(String baseDirPath, String beginFolderName, int maxFolderLevels, String extractFilePath, String outputXmlPath) {
        this();
        configKeyValueMap.put(Constants.Base_Dir_Path, baseDirPath);
        configKeyValueMap.put(Constants.Beigin_Folder_Name, beginFolderName);
        configKeyValueMap.put(Constants.Max_Folder_Levels, String.valueOf(maxFolderLevels));
        configKeyValueMap.put(Constants.Extract_File_Path, extractFilePath);
        configKeyValueMap.put(Constants.Output_Xml_Path, outputXmlPath);
    }

    public static String getConfigKeyValue(String key) {
        return configKeyValueMap.get(key).trim();
    }

    public static void setSuiteNodeClassNameMap(Map<String, List<String>> map) {
        suiteNodeClassNameMap = map;
    }

    public static Map<String, List<String>> getSuiteNodeClassNameMap() {
        return suiteNodeClassNameMap;
    }

    private static Map<String, String> loadConfiguration(String configName, String... configKeys) {
        Properties properties = new Properties();
        Map<String, String> configKeyValueMap = new HashMap<String, String>();
        InputStream inputStream = BaseHelper.class.getResourceAsStream("/" + configName);
        try {
            properties.load(inputStream);
            configKeyValueMap.clear();
            for (String key : configKeys) {
                String value = properties.getProperty(key).trim();
                configKeyValueMap.put(key, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return configKeyValueMap;
    }

    public static void addNodeClassName2Map(String fullClassName, String methodName) {
        List<String> ownMethodNames = new ArrayList<String>();
        if (!suiteNodeClassNameMap.containsKey(fullClassName)) {
            ownMethodNames.add(methodName);
            suiteNodeClassNameMap.put(fullClassName, ownMethodNames);
        } else {
            boolean isDuplicated = false;
            ownMethodNames = suiteNodeClassNameMap.get(fullClassName);
            for (String tmpName : ownMethodNames) {
                if (tmpName.equals(methodName)) {
                    isDuplicated = true;
                    break;
                }
            }
            if (!isDuplicated) ownMethodNames.add(methodName);
        }
    }
}
