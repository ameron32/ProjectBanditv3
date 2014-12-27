package com.ameron32.apps.projectbanditv3.object;

import com.parse.ParseClassName;


@ParseClassName("CAdv3GURPS")
public class Advantage extends AbsBanditObject<Advantage.Column> {
	
	/*
	 * OUTDATED:
	 * Advantages are created by the AdvantageEditor by loading a .CSV file into the system.
	 * MOST variables are imported through this process.
	 * Other variables are set by the user after the fact.
	 */
   

	
	public Advantage() {
	  // REQUIRED EMPTY CONSTRUCTOR (PARSE)
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(); 
		sb.append("Advantage: ");
		for (Column c : columns) {
		  sb.append("\n  ");
		  sb.append(c.key);
		  sb.append(": ");
		  final String shorten = asString(c);
		  sb.append(shorten);
		}
		return sb.toString();
	}

  public static class Column extends AbsBanditObject.Column {
    
    public Column(String key, DataType dataType) {
      super(key, dataType);
    }
  }
   
  private static final String SNAME = "sName";
  private static final String SDESCRIPTION = "sDescription";
  private static final String SID = "sId";
  private static final String IIDX = "iIdx";
  private static final String SADPQ = "sADPQ";
  private static final String SADVTYPE = "sAdvType";
  private static final String SSUPERTYPE = "sSuperType";
  private static final String SBOOKCOST = "sBookCost";
  private static final String IPAGE = "iPage";
  private static final String BISLEVELED = "bIsLeveled";
  private static final String BISMULTICOST = "bIsMultiCost";
  private static final String BISVARIABLECOST = "bIsVariableCost";
  private static final String IBASECOST = "iBaseCost";
  private static final String SMULTICOST = "sMultiCost";
  private static final String IPERLEVELCOST = "iPerLevelCost";
  private static final String SPERLEVELMULTICOST = "sPerLevelMultiCost";
  private static final String BHASNOTES = "bHasNotes";
  private static final String BISFAKECOST = "bIsFakeCost";
  private static final String ICALCCOST = "iCalcCost";
  private static final String SLISTPMSESM = "sListPMSESM";
  private static final String SREFS = "sRefs";
  private static final String SDOCUMENTSOURCE = "sDocumentSource";
  private static final String BISFORBIDDEN = "bIsForbidden";
  
  private static Column[] columns = {
    new Column(SNAME, DataType.String),
    new Column(SDESCRIPTION, DataType.String),
    new Column(SID, DataType.String),
    new Column(IIDX, DataType.Integer),
    new Column(SADPQ, DataType.String),
    new Column(SADVTYPE, DataType.String),
    new Column(SSUPERTYPE, DataType.String),
    new Column(SBOOKCOST, DataType.String),
    new Column(IPAGE, DataType.Integer),
    new Column(BISLEVELED, DataType.Boolean),
    new Column(BISMULTICOST, DataType.Boolean),
    new Column(BISVARIABLECOST, DataType.Boolean),
    new Column(IBASECOST, DataType.Integer),
    new Column(SMULTICOST, DataType.String),
    new Column(IPERLEVELCOST, DataType.Integer),
    new Column(SPERLEVELMULTICOST, DataType.String),
    new Column(BHASNOTES, DataType.Boolean),
    new Column(BISFAKECOST, DataType.Boolean),
    new Column(ICALCCOST, DataType.Integer),
    new Column(SLISTPMSESM, DataType.ListOfStrings),
    new Column(SREFS, DataType.String),
    new Column(SDOCUMENTSOURCE, DataType.String),
    new Column(BISFORBIDDEN, DataType.Boolean)
  };
  
  @Override public Advantage.Column get(
      int columnPosition) {
    return columns[columnPosition];
  }

  @Override public int getColumnCount() {
    return columns.length;
  }
}
