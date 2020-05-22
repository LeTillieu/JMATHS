package sample;

public class ColData {
    private Integer rowNum;
    private String coll1Data;
    private String coll2Data;

    /**
     * @param rowNum the id of the row
     * @param coll1Data the value column
     * @param coll2Data the Effective column
     */
    public ColData(Integer rowNum, String coll1Data, String coll2Data){
        this.rowNum = rowNum;
        this.coll1Data = coll1Data;
        this.coll2Data = coll2Data;
    }


    public Integer getRowNum() {
        return rowNum;
    }


    public void setColl1Data(String coll1Data) {
        this.coll1Data = coll1Data;
    }

    public void setColl2Data(String coll2Data) {
        this.coll2Data = coll2Data;
    }

    public String getColl1Data() {
        return coll1Data;
    }

    public String getColl2Data() {
        return coll2Data;
    }
}
