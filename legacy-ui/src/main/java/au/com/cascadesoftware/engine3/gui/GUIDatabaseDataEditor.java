package au.com.cascadesoftware.engine3.gui;

/*import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.internet.mysql.Database;
import au.com.cascadesoftware.engine3.internet.mysql.Table;

public abstract class GUIDatabaseDataEditor extends GUI {

	private GUIDatabaseTable[] tableGuis;
	private GUITabs tabs;
	
	public GUIDatabaseDataEditor(Window window, Boundary bounds, Database db) {
		super(window, bounds);
		String[] tableNames = db.getTableNames();
		for(String s : tableNames) s = (s.charAt(0) + "").toUpperCase() + s.toLowerCase().substring(1);
		tabs = new GUITabs(window, new Boundary(), tableNames, Theme.TEST1);
		onResize();
		addGUI(tabs);
		Table[] tables = getTables();
		tableGuis = new GUIDatabaseTable[tables.length];
		for(int i = 0; i < tables.length; i++){
			final int iF = i;
			tableGuis[i] = new GUIDatabaseTable(window, new Boundary(), tables[i], Theme.TEST1){
				@Override
				protected void onUpdate() {
					for(int j = 0; j < tables.length; j++) if(iF != j) tableGuis[j].forceUpdate();
					Table[] tables = getTables();
					for(int j = 0; j < tables.length; j++){
						tableGuis[j].updateTable(tables[j]);
					}
				}
			};
			tabs.addGUI(tableGuis[i], i);
		}
	}

	protected abstract Table[] getTables();

}*/
