package com.herokuapp.theinternet.base;

import org.testng.annotations.DataProvider;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CsvDataProviders {
    @DataProvider(name="csvReader")
    public static Iterator<Object[]> csvReader (Method method){
        List<Object> list  = new ArrayList<Object>();
        String pathName = "src"+ File.separator+"test"+File.separator+"resources" + File.separator+"dataproviders"+File.separator+method.getDeclaringClass().getSimpleName()+File.separator+method.getName()+".csv";
        return null;
    }
}
