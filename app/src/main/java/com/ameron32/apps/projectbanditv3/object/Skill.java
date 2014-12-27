package com.ameron32.apps.projectbanditv3.object;

import com.parse.ParseClassName;


@ParseClassName("CSkill3GURPS")
public class Skill extends AbsBanditObject<Skill.Column> {
	
	/*
	 * OUTDATED:
	 * Advantages are created by the AdvantageEditor by loading a .CSV file into the system.
	 * MOST variables are imported through this process.
	 * Other variables are set by the user after the fact.
	 */
   

	
	public Skill() {
	  // REQUIRED EMPTY CONSTRUCTOR (PARSE)
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(); 
		sb.append("Skill: ");
		for (Column c : columns) {
		  sb.append("\n  ");
		  sb.append(c.key);
		  sb.append(": ");
		  final String shorten = asString(c);
		  sb.append(shorten);
		}
		return sb.toString();
	}

	
	
	/*
	public void pullData() {
	  int calcCost = this.getInt("iCalcCost");
	  int id = this.getInt("iId");
	  String aORd = this.getString("sAorD");
	  String nameString = this.getString("sName");
	  String advTypeString = this.getString("sAdvType");
	  String superTypeString = this.getString("sSuperType");
//	  String cost = this.getString("iCost");
	  int pageInt = this.getInt("iPage");
	  boolean isLeveled = this.getBoolean("isLeveled");
//    boolean hasNotes = this.getBoolean("hasNotes");
	  boolean isFakeCost = this.getBoolean("isFakeCost");
//    boolean isPhysical = this.getBoolean("isPhysical");
//    boolean isMental = this.getBoolean("isMental");
//    boolean isSocial = this.getBoolean("isSocial");
//    boolean isExotic = this.getBoolean("isExotic");
//    boolean isSuper = this.getBoolean("isSuper");
//    boolean isMundane = this.getBoolean("isMundane");
	  boolean isForbidden = this.getBoolean("isForbidden");
    // FIXME I cheated to get the front and back off of the
    // description.
    // Once proper punctuation is handled, this will cause problems.
    String description = this.getString("sDescription");
//    .substring(3, this.getString("description").length() - 3);
    
    setPMSESM(this.getString("sListPMSESM"));
    setDetails(id, 0, aORd, nameString, advTypeString, superTypeString, "", pageInt, isLeveled, isFakeCost, calcCost, isPhysical, isMental, isSocial, isExotic, isSuper, isMundane, isForbidden, description);
	}
	
  private void setPMSESM(String list) {
//    boolean isPhysical;
//    boolean isMental;
//    boolean isSocial;
//    boolean isExotic;
//    boolean isSuper;
//    boolean isMundane;
    
    final String[] sTmp = list.split(";");
    final int[] iTmp = new int[sTmp.length];
    
    for (int i = 0; i < sTmp.length; i++) {
      final String t = sTmp[i];
      sTmp[i] = t.replace(";", "");
      iTmp[i] = Integer.valueOf(sTmp[i]);
      final boolean is = (iTmp[i] == 1);
      
      switch (i) {
      case 0:
        isPhysical = is;
        break;
      case 1:
        isMental = is;
        break;
      case 2:
        isSocial = is;
        break;
      case 3:
        isExotic = is;
        break;
      case 4:
        isSuper = is;
        break;
      case 5:
        isMundane = is;
        break;
      default:
        // unreachable
      }
    }
	}
	*/

  public static class Column extends AbsBanditObject.Column {
    
    public Column(String key, DataType dataType) {
      super(key, dataType);
    }
  }
  
//  // irrelevant data
//  int id;
//  int ver;
//
//  // key user-facing data
//  String nameString;
//  String description;
//  
//  // grouping data
//  boolean isForbidden;
//  String aORd;
//  String advTypeString;
//  String superTypeString;
//
//  // cost data
//  String cost;
//  int calcCost;
//  boolean isFakeCost;
//  boolean isLeveled;
//
//  // category data
//  boolean isPhysical;
//  boolean isMental;
//  boolean isSocial;
//  boolean isExotic;
//  boolean isSuper;
//  boolean isMundane;
//
//  // reference data
//  int pageInt;
//
//  // optional data
////  boolean hasNotes;
////  String myNotes;
  

  
  private static final String SNAME = "sName";
  private static final String SDESCRIPTION = "sDescription";
  private static final String SID = "sId";
  private static final String BISLEVELED = "bIsLeveled";
  private static final String SATTRIBUTE = "sAttribute";
  private static final String SDIFFICULTY = "sDifficulty";
  private static final String BHASSUBSKILLS = "bHasSubSkills";
  private static final String SLISTDEFAULTS = "sListDefaults";
  private static final String BEXIST = "bExist";
  private static final String SALTATT = "sAltAtt";
  private static final String SALTDIFF = "sAltDiff";
  private static final String SLISTCATEGORIESANDSUBCATEGORIES = "sListCategoriesAndSubCategories";
  private static final String BREQUIRESSPECIALIZATION = "bRequiresSpecialization";
  private static final String IPAGE = "iPage";
  private static final String SDOCUMENTSOURCE = "sDocumentSource";
  
  private static Column[] columns = {
    new Column(SNAME, DataType.String),
    new Column(SDESCRIPTION, DataType.String),
    new Column(SID, DataType.String),
    new Column(BISLEVELED, DataType.Boolean),
    new Column(SATTRIBUTE, DataType.String),
    new Column(SDIFFICULTY, DataType.String),
    new Column(BHASSUBSKILLS, DataType.Boolean),
    new Column(SLISTDEFAULTS, DataType.ListOfStrings),
    new Column(BEXIST, DataType.Boolean),
    new Column(SALTATT, DataType.String),
    new Column(SALTDIFF, DataType.String),
    new Column(SLISTCATEGORIESANDSUBCATEGORIES, DataType.ListOfStrings),
    new Column(BREQUIRESSPECIALIZATION, DataType.Boolean),
    new Column(IPAGE, DataType.Integer),
    new Column(SDOCUMENTSOURCE, DataType.String)
  };
  
  @Override public Skill.Column get(
      int columnPosition) {
    return columns[columnPosition];
  }

  @Override public int getColumnCount() {
    return columns.length;
  }
}
