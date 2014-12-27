package com.ameron32.apps.projectbanditv3.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.view.TableRowLayout;

import java.util.ArrayList;
import java.util.List;

public class TableAdapter<T extends Object>
    extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
  private Columnable<T> mHeaderObject;
  private List<Columnable<T>> mDataset;
  private int mRowLayoutResource;
  private int mCellLayoutResource;
  private int mTextViewResourceId;
  
  public static class ViewHolder extends
      RecyclerView.ViewHolder {
    public TableRowLayout mTableRowLayoutView;
    
    public ViewHolder(TableRowLayout v) {
      super(v);
      mTableRowLayoutView = v;
    }
  }
  
//  public TableAdapter(
//      List<? extends Columnable<T>> myDataset,
//      int rowLayoutResource,
//      int cellLayoutResource,
//      int textViewResourceId) {
//    mDataset = myDataset.toArray(new Columnable[myDataset.size()]);
//    mRowLayoutResource = rowLayoutResource;
//    mCellLayoutResource = cellLayoutResource;
//    mTextViewResourceId = textViewResourceId;
//  }
//  
//  public TableAdapter(
//      List<? extends Columnable<T>> myDataset,
//      int rowLayoutResource) {
//    this(myDataset, rowLayoutResource, R.layout.simple_table_cell_textview_container, R.id.textview);
//  }
//  
//  public TableAdapter(
//      List<? extends Columnable<T>> myDataset,
//      int cellLayoutResource,
//      int textViewResourceId) {
//    this(myDataset, R.layout.simple_table_row_layout, cellLayoutResource, textViewResourceId);
//  }
  
  public TableAdapter(
      List<Columnable<T>> myDataset,
      int rowLayoutResource,
      int cellLayoutResource,
      int textViewResourceId) {
    if (myDataset == null) { mDataset = new ArrayList<Columnable<T>>(); }
    else { mDataset = myDataset; }
    mRowLayoutResource = rowLayoutResource;
    mCellLayoutResource = cellLayoutResource;
    mTextViewResourceId = textViewResourceId;
  }
  
  public TableAdapter(
      List<Columnable<T>> myDataset,
      int rowLayoutResource) {
    this(myDataset, rowLayoutResource, R.layout.simple_table_cell_textview_container, R.id.textview);
  }
  
  public TableAdapter(
      List<Columnable<T>> myDataset,
      int cellLayoutResource,
      int textViewResourceId) {
    this(myDataset, R.layout.simple_table_row_layout, cellLayoutResource, textViewResourceId);
  }
  
  // Create new rows
  @Override public TableAdapter.ViewHolder onCreateViewHolder(
      ViewGroup parent, int viewType) {
    TableRowLayout trl = createRowView(parent, viewType);
    
    // return furnished viewholder
    TableAdapter.ViewHolder vh = new ViewHolder(trl);
    return vh;
  }

  private TableRowLayout createRowView(
      ViewGroup parent, int viewType) {
    // inflate from template layout XML
    TableRowLayout trl = (TableRowLayout) LayoutInflater.from(parent.getContext()).inflate(mRowLayoutResource, parent, false);
    
    // set the view's size, margins, paddings and layout parameters
    onSetTableRowAttributes(trl, parent, viewType);
    
    // inflate cells into row
    int firstRowColumnCount = mDataset.get(0).getColumnCount();
    trl.inflateColumns(firstRowColumnCount, mCellLayoutResource, parent);
    return trl;
  }
  
  public void setHeaderObject(Columnable<T> headerObject) {
    // sanitize object?
    mHeaderObject = headerObject;
  }
  
  public TableRowLayout getHeaderRow(ViewGroup parent) {
    // TODO: Header View Type?
    final int HEADER_VIEW_TYPE = 0;
    TableRowLayout trl = createRowView(parent, HEADER_VIEW_TYPE);
    
    populateRow(mHeaderObject, trl);
    return trl;
  }
  
  /**
   * Set the view's size, margins, paddings and layout parameters within this method.
   * @param trl
   * @param parent
   * @param viewType
   */
  protected void onSetTableRowAttributes(
      TableRowLayout trl,
      ViewGroup parent, int viewType) {
    // allow subclass to Override
  }
  
  // Populate row
  @Override public void onBindViewHolder(
      TableAdapter.ViewHolder holder,
      int position) {
    final Columnable<T> row = mDataset.get(position);
    
    int columnCount = row.getColumnCount();
    final int maxColumnCount = mDataset.get(0).getColumnCount();
    // use the smaller of the two column counts, firstRow & thisRow
    columnCount = (columnCount < maxColumnCount) 
        ? columnCount : maxColumnCount;
    
    Columnable<T> object = mDataset.get(position);
    populateRow(object, holder.mTableRowLayoutView);
  }
  
  private void populateRow(Columnable<T> object, TableRowLayout layout) {
    // loop through all cells, populating them with the appropriate data
    int columnCount = object.getColumnCount();
    for (int i = 0; i < columnCount; i++) {
      final int columnPosition = i;
      String columnString = null;
      if (object.isHeaderView()) {
        columnString = object.getColumnHeader(columnPosition);
      } else {
        columnString = object.get(columnPosition).toString();
      }
      layout.populateColumnTextView(columnPosition, columnString, mTextViewResourceId);
    }
  }
  
  /**
   * Get number of rows in table.
   * @return Number of rows in the table.
   */
  @Override public int getItemCount() {
    // Get number of rows
    return mDataset.size();
  }
  
  /**
   * Get object @ cell x,y.
   * @param rowPosition
   * @param columnPosition
   * @return Object corresponding to the given cell location.
   */
  public T getItemAt(int rowPosition,
      int columnPosition) {
    // Get object @ cell x,y
    return mDataset.get(rowPosition).get(columnPosition);
  }
  
  public interface Columnable<T> {
    public T get(int columnPosition);
    
    public int getColumnCount();
    
    public String getColumnHeader(int columnPosition);
    
    public void useAsHeaderView(boolean b);
    
    public boolean isHeaderView();
  }
}