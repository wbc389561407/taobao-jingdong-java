package com.jiujiu;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 解决无法插入行和列问题
 * @author wangbingchen
 * @Description
 * @create 2022-01-10 9:07
 *
 * 需要的依赖
 *
          <dependency>
              <groupId>org.apache.poi</groupId>
              <artifactId>poi</artifactId>
              <version>4.1.0</version>
          </dependency>

          <dependency>
              <groupId>org.apache.poi</groupId>
              <artifactId>poi-ooxml</artifactId>
              <version>4.1.0</version>
          </dependency>
 */
public class ExcelUtil {


    public static void main(String[] args) {

//        File file = new File("C:\\Users\\aiwan\\Desktop\\123\\123.xlsx");
//        boolean newFile = file.createNewFile();
//        System.out.println(newFile);

        ExcelUtil excelUtil = new ExcelUtil("C:\\Users\\Administrator\\Desktop\\产品列表.xls");
        List<String> oneColumnData = excelUtil.getOneColumnData(1);
        for (String oneColumnDatum : oneColumnData) {
            System.out.println(oneColumnDatum);
        }
        excelUtil.writeAndClose();


    }

    private static void copyFile(String filePath, String bakPath) {
        File file = new File(filePath);
        if (!file.exists()){
            return;
        }
        File bakFile = new File(bakPath);
//        if (file.exists()){
//            String name = bakFile.getName();
//            String[] split = name.split("\\.");
//            bakFile = new File(bakFile.getParent()+"\\"+split[0]+"-"+DateUtil.now("yyyyMMddHHmmss")+"."+split[1]);
//        }
        try {
            Files.copy(file.toPath(), bakFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //一个表格
    private Workbook workbook = null;
    //页码
    private Sheet sheetAt = null;
    //文件路径
    private String filePath = null;
    //操作的文件
    private File file = null;

    private ExcelUtil(){};


    public ExcelUtil(File file){
        this.file = file;
        loadFile();

    }

    private void loadFile() {
        //判断file 是否存在 不存在就创建一个
        initFile();
        //加载表格所需对象
        readExcel();
    }

    public ExcelUtil(String filePath){
        this.file = new File(filePath);
        loadFile();
    }


    //写一行
    public void setOneRowData(List<String> list, int row){
        for (int i = 0; i < list.size(); i++) {
            setStringVal(list.get(i),row,i);
        }
    }

    public void setOneRowData(List<String> list){
        int allRowCount = getAllRowCount();
        for (int i = 0; i < list.size(); i++) {
            setStringVal(list.get(i),allRowCount,i);
        }
    }



    //写一列 指定第几行开始写 控制间隔 0 就是没有间隔 1 就是隔一行
    public void setOneColumnData(List<String> list, String columnName, int row , int interval){
        setOneColumnData(list,getIndex(columnName),row,interval);
    }


    //写一列 指定第几行开始写
    public void setOneColumnData(List<String> list, String columnName, int row){
        setOneColumnData(list,getIndex(columnName),row,0);
    }


    //写一列
    public void setOneColumnData(List<String> list, String columnName){
        setOneColumnData(list,getIndex(columnName),0,0);
    }

    //写一列
    public void setOneColumnData(List<String> list, int columnNum, int row, int interval){
        interval++;
        for (int i = 0; i < list.size(); i++) {
            setStringVal(list.get(i),i*interval+row,columnNum);
        }
    }


    /**
     *
     * @param text 单元格内容
     * @param rowIndex 行下标 第一行为 0
     * @param index 列下标 第一列为 0
     */
    public void setIntVal(Integer text, int rowIndex,int index) {
        //创建第一行
        Row row = sheetAt.getRow(rowIndex);
        if(row==null){
            row = sheetAt.createRow(rowIndex);
        }
        //新建单元格
        Cell cell = row.createCell(index);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);

        cell.setCellValue(text);
    }



    /**
     *
     * @param text 单元格内容
     * @param rowIndex 行下标 第一行为 0
     * @param index 列下标 第一列为 0
     */
    public void setStringVal(String text, int rowIndex,int index) {
        //创建第一行
        Row row = sheetAt.getRow(rowIndex);
        if(row==null){
            row = sheetAt.createRow(rowIndex);
        }
        //新建单元格
        Cell cell = row.createCell(index);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);

        cell.setCellValue(text);
    }

    /**
     * 设置指定单元格内容
     * @param text 设置的内容
     * @param cellName 单元格名称 A1
     */
    public void setStringVal(String text, String cellName) {
        int index = stringToIndex(cellName);
        int rowIndex = stringToRowIndex(cellName);
        setStringVal(text,rowIndex,index);
    }


    /**
     * 获取指定单元格的值
     * @param str  单元格名称 A8
     * @return
     */
    public String getStringVal(String str) {
        int index = stringToIndex(str);
        int rowIndex = stringToRowIndex(str);
        return getStringVal(rowIndex,index);
    }

    private int stringToRowIndex(String str) {
        str = str.toUpperCase();
        return Integer.parseInt(str.replaceAll("[A-Z]", ""))-1;//得到行数 减去1
    }

    private int stringToIndex(String str) {
        str = str.toUpperCase();
        return getIndex(str.replaceAll("\\d", "")); //得到字母转换为列下标
    }


    /**
     * 获取指定单元格的值
     * @param rowIndex 第一行传 0
     * @param cellNum 第一格传 0
     */
    public String getStringVal(int rowIndex, int cellNum) {
        Row row = sheetAt.getRow(rowIndex); // 获取第一行
        if(row==null){
//            System.out.println("这一行没有数据了");
            return "";
        }
        Cell cell = row.getCell(cellNum); // 获取第一格
        if(cell==null){
            return "";
        }

        return getStringVal(cell);
    }


    /**
     * 获取指定列的所有值
     * @param column B
     * @return B列的所有数据 list
     */
    public List<String> getOneColumnData(String column){
        return getOneColumnData(getIndex(column));
    }


    /**
     * 获取指定列的所有值
     * @param column 传入 0
     * @return 第一列的所有数据
     */
    public List<String> getOneColumnData(int column){
        int allRowCount = this.getAllRowCount();
        List<String> strings = new ArrayList<>(allRowCount);
//        System.out.println(allRowCount);
        for (int i = 0; i < allRowCount; i++) {
            String val = this.getStringVal(i, column);
            if(val==null||val.length()==0){
                continue;
            }
            strings.add(val);
        }
        return strings;
    }



    //表行 字母 转 下标 A>0,B>1,AA>26
    private int getIndex(String string){
        string = string.toUpperCase();
        char[] chars = string.toCharArray();
        int index = 0;
        for (int i = 0; i < chars.length; i++) {
            index += ((int)Math.pow(26,chars.length-1-i))*(getIndex(chars[i])+1);
        }
        return index-1;
    }

    //字母转数字
    private int getIndex(char ch){
        return ch-65;
    }

    /**
     *
     * @return 本页的总行数
     */
    public int getAllRowCount() {
        return sheetAt.getLastRowNum()+1;
    }

    private String getStringVal(Cell cell) {
        return getStringVal(cell,null);

    }

    //
    private String getStringVal(Cell cell,CellType cellType) {
        String reVal = null;
        if(cellType==null){
            cellType = cell.getCellType();
        }
//        System.out.println(cellType.toString());

        if("_NONE".equals(cellType.toString())){
            reVal = "";
        }

        if("NUMERIC".equals(cellType.toString())){
            //数字
            double numericCellValue = cell.getNumericCellValue();
            reVal = new BigDecimal(numericCellValue).toString();
        }

        if("STRING".equals(cellType.toString())){
            //文本
            reVal = cell.getStringCellValue();
        }

        if("FORMULA".equals(cellType.toString())){
            //公式
            reVal = getStringVal(cell, cell.getCachedFormulaResultType());
        }

        if("BLANK".equals(cellType.toString())){
            reVal = cell.getStringCellValue();
        }

        if("BOOLEAN".equals(cellType.toString())){
            reVal = Boolean.toString(cell.getBooleanCellValue());
        }

        if("ERROR".equals(cellType.toString())){
            reVal = Byte.toString(cell.getErrorCellValue());
        }

        return reVal;
    }






    //获得一个表格 判断版本
    private void readExcel() {
        if(!file.exists()){
            System.out.println("文件不存在");
            return;
        }
        String name = file.getName();
        String[] split = name.split("\\.");


        Workbook workbook = null;
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            if("xls".equals(split[1])){
                workbook = new HSSFWorkbook(inputStream); //xls
            }

            if("xlsx".equals(split[1])){
                workbook = new XSSFWorkbook(inputStream); //xlsx
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.workbook = workbook;
        this.sheetAt = this.workbook.getSheetAt(0);
        this.filePath = file.getPath();
//        this.file = new File(this.filePath);
    }

    private void initFile() {

        if(!file.exists()){
            System.out.println("initFile:file is null System creating");
            createFile();
            System.out.println("file created success!");
        }
    }

    private void createFile() {
        String name = file.getName();
        String[] split = name.split("\\.");

        Workbook workbook = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if("xls".equals(split[1])){
                workbook = new HSSFWorkbook();//xls
            }

            if("xlsx".equals(split[1])){
                workbook = new XSSFWorkbook(); //xlsx
            }
            workbook.createSheet("Sheet1");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeAndClose() {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(filePath);
            workbook.write(fout);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                workbook.close();
                assert fout != null;
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
