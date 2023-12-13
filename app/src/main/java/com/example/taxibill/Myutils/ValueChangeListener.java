package com.example.taxibill.Myutils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ValueChangeListener {
    public static int myValue;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    public void setMyValue(int newValue) {
        int oldValue = myValue;
        myValue = newValue;
        propertyChangeSupport.firePropertyChange("myValue", oldValue, newValue);
    }

    public int getMyValue() {
        return myValue;
    }

}
