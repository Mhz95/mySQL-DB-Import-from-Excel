import java.util.List;

public class GeneralTable {
	
	String tableName;
	String headers[];
	String types[];
	List<Object[]> rows;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String[] getHeaders() {
		return headers;
	}
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	public String[] getTypes() {
		return types;
	}
	public void setTypes(String[] types) {
		this.types = types;
	}
	public List<Object[]> getRows() {
		return rows;
	}
	public void setRows(List<Object[]> rows) {
		this.rows = rows;
	}

}
