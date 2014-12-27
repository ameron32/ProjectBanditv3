package com.ameron32.apps.projectbanditv3.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A Spinner view that does not dismiss the dialog displayed when the control is
 * "dropped down" and the user presses it. This allows for the selection of more
 * than one option.
 */
public class MultiSelectObjectSpinner<T> extends Spinner implements OnMultiChoiceClickListener {
  
  public static class Item<T> {
    public String mLabel;
    public boolean mSelected;
    public T mItem;
    public Item(String label, T item) {
      mLabel = label;
      mItem = item;
      mSelected = false;
    }
  }
  
  private boolean[] getSelection() {
    final int size = _items.length;
    final boolean[] selection = new boolean[size];
    for (int i = 0; i < size; i++) {
      final Item<T> item = _items[i];
      selection[i] = item.mSelected;
    }
    return selection;
  }
  
  private void setSelectedAt(int position, boolean isSelected) {
    _items[position].mSelected = isSelected;
  }
  
  private String[] getItemLabels() {
    final int size = _items.length;
    final String[] labels = new String[size];
    for (int i = 0; i < size; i++) {
      final Item<T> item = _items[i];
      labels[i] = item.mLabel;
    }
    return labels;
  }
  
  private List<T> getItems() {
    final int size = _items.length;
    List<T> items = new ArrayList<T>();
    for (int i = 0; i < size; i++) {
      final Item<T> item = _items[i];
      items.add(item.mItem);
    }
    return items;
  }
  
//  String[] _items = new String[0];
//  boolean[] _selection = new boolean[0];
  Item<T>[] _items;
  
  ArrayAdapter<String> _proxyAdapter;
  
  /**
   * Constructor for use when instantiating directly.
   * 
   * @param context
   */
  public MultiSelectObjectSpinner(Context context) {
    super(context);
    initialize(context);
  }

  /**
   * Constructor used by the layout inflater.
   * 
   * @param context
   * @param attrs
   */
  public MultiSelectObjectSpinner(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize(context);
  }
  
  public MultiSelectObjectSpinner(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initialize(context);
  }
  
  private void initialize(Context context) {
    _proxyAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
    super.setAdapter(_proxyAdapter);
  }
  
  
  
  
  
  

  /**
   * {@inheritDoc}
   */
  @Override
  public void onClick(DialogInterface dialog, int which, boolean isChecked) {
    boolean[] selection = getSelection();
    if (selection != null && which < selection.length) {
      setSelectedAt(which, isChecked);
      
      _proxyAdapter.clear();
      _proxyAdapter.add(buildSelectedItemString());
      // TODO: review!
      setSelection(0);
    }
    else {
      throw new IllegalArgumentException("Argument 'which' is out of bounds.");
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean performClick() {
//    boolean unused = super.performClick();
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setMultiChoiceItems(getItemLabels(), getSelection(), this);
    builder.show();
    return true;
  }
  
  /**
   * MultiSelectSpinner does not support setting an adapter. This will throw an
   * exception.
   * 
   * @param adapter
   */
  @Override
  public void setAdapter(SpinnerAdapter adapter) {
    throw new RuntimeException("setAdapter is not supported by MultiSelectSpinner.");
  }
  
//  /**
//   * Sets the options for this spinner.
//   * 
//   * @param items
//   */
//  public void setItems(String[] items) {
//    _items = items;
//    _selection = new boolean[_items.length];
//    
//    Arrays.fill(_selection, false);
//  }
//  
//  /**
//   * Sets the options for this spinner.
//   * 
//   * @param items
//   */
//  public void setItems(List<String> items) {
//    _items = items.toArray(new String[items.size()]);
//    _selection = new boolean[_items.length];
//    
//    Arrays.fill(_selection, false);
//  }
  
  /**
   * Sets the options for this spinner.
   * 
   * @param items
   */
  public void setItems(Item<T>[] items) {
    _items = items;
  }
  
  /**
   * Sets the options for this spinner.
   * 
   * @param items
   */
  public void setItems(List<Item<T>> items) {
    setItems(items.toArray(new Item[items.size()]));
  }
  
//  /**
//   * Sets the selected options based on an array of string.
//   * 
//   * @param selection
//   */
//  public void setSelection(String[] selection) {
//    for (String sel : selection) {
//      for (int j = 0; j < _items.length; ++j) {
//        if (_items[j].equals(sel)) {
//          _selection[j] = true;
//        }
//      }
//    }
//  }
//  
//  /**
//   * Sets the selected options based on a list of string.
//   * 
//   * @param selection
//   */
//  public void setSelection(List<String> selection) {
//    for (String sel : selection) {
//      for (int j = 0; j < _items.length; ++j) {
//        if (_items[j].equals(sel)) {
//          _selection[j] = true;
//        }
//      }
//    }
//  }
  
  /**
   * Sets the selected options based on a list of objects.
   * 
   * @param selection
   */
  public void setSelection(T[] selection) {
    for (T sel : selection) {
      for (int j = 0; j < _items.length; ++j) {
        if (_items[j].mItem.equals(sel)) {
          setSelectedAt(j, true);
        }
      }
    }
  }
  
  public void setSelection(List<T> selection) {
    for (T sel : selection) {
      for (int j = 0; j < _items.length; ++j) {
        if (_items[j].mItem.equals(sel)) {
          setSelectedAt(j, true);
        }
      }
    }
  }
  
  /**
   * Sets the selected options based on an array of positions.
   * 
   * @param selectedIndicies
   */
  public void setSelection(int[] selectedIndicies) {
    for (int index : selectedIndicies) {
      if (index >= 0 && index < _items.length) {
        setSelectedAt(index, true);
      }
      else {
        throw new IllegalArgumentException("Index " + index + " is out of bounds.");
      }
    }
  }
  
//  public void setSelectedIndex(int selected) {
//    int[] selection = new int[1];
//    selection[0] = selected;
//    setSelection(selection);
//    
//    _proxyAdapter.clear();
//    _proxyAdapter.add(buildSelectedItemString());
//    setSelection(0);
//  }
  
  /**
   * Returns a list of positions, one for each selected item.
   * 
   * @return
   */
  public List<T> getSelectedItems() {
    List<T> selection = new LinkedList<T>();
    for (int i = 0; i < _items.length; ++i) {
      if (_items[i].mSelected) {
        selection.add(_items[i].mItem);
      }
        
//      if (_selection[i]) {
//        selection.add(i);
//      }
    }
    return selection;
  }
    
  /**
   * Returns a list of strings, one for each selected item.
   * 
   * @return
   */
  public List<String> getSelectedStrings() {
    List<String> selection = new LinkedList<String>();
    for (int i = 0; i < _items.length; ++i) {
      if (_items[i].mSelected) {
        selection.add(_items[i].mLabel);
      }
      
//      if (_selection[i]) {
//        selection.add(_items[i]);
//      }
    }
    return selection;
  }
    
  /**
   * Returns a list of positions, one for each selected item.
   * 
   * @return
   */
  public List<Integer> getSelectedIndicies() {
    List<Integer> selection = new LinkedList<Integer>();
    for (int i = 0; i < _items.length; ++i) {
      if (_items[i].mSelected) {
        selection.add(i);
      }
        
//      if (_selection[i]) {
//        selection.add(i);
//      }
    }
    return selection;
  }
    
  /**
   * Builds the string for display in the spinner.
   * 
   * @return comma-separated list of selected items
   */
  private String buildSelectedItemString() {
    StringBuilder sb = new StringBuilder();
    boolean foundOne = false;
    
    for (int i = 0; i < _items.length; ++i) {
      if (_items[i].mSelected) {
        if (foundOne) {
          sb.append(", ");
        }
        foundOne = true;
        
        sb.append(_items[i].mLabel);
      }
    }
    
    return sb.toString();
  }
}