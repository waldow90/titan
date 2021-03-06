package com.thinkaurelius.titan.util.system;

import com.google.common.base.Preconditions;
import org.apache.commons.configuration.Configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Matthias Broecheler (me@matthiasb.com)
 */
public class ConfigurationUtil {

    private static final char CONFIGURATION_SEPARATOR = '.';

    public static List<String> getUnqiuePrefixes(Configuration config) {
        Set<String> nameSet = new HashSet<String>();
        List<String> names = new ArrayList<String>();
        Iterator<String> keyiter = config.getKeys();
        while (keyiter.hasNext()) {
            String key = keyiter.next();
            int pos = key.indexOf(CONFIGURATION_SEPARATOR);
            if (pos > 0) {
                String name = key.substring(0, pos);
                if (nameSet.add(name)) {
                    names.add(name);
                }
            }
        }
        return names;
    }

    public final static <T> T instantiate(String clazzname) {
        return instantiate(clazzname,new Object[0],new Class[0]);
    }

    public final static <T> T instantiate(String clazzname, Object[] constructorArgs, Class[] classes) {
        Preconditions.checkArgument(constructorArgs!=null && classes!=null);
        Preconditions.checkArgument(constructorArgs.length==classes.length);
        try {
            Class clazz = Class.forName(clazzname);
            Constructor constructor = clazz.getConstructor(classes);
            T instance = (T) constructor.newInstance(constructorArgs);
            return instance;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not find implementation class: " + clazzname, e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Implementation class does not have required constructor: " + clazzname, e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Could not instantiate implementation: " + clazzname, e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Could not instantiate implementation: " + clazzname, e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Could not instantiate implementation: " + clazzname, e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Could not instantiate implementation: " + clazzname, e);
        }
    }

}
