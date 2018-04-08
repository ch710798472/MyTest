package com.test.ch.domain.poi;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.common.lang.io.ByteArrayOutputStream;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * @author banmo
 * @create 2018-04-08
 **/
public class POIUtils {
    private static final Logger log = LoggerFactory.getLogger(POIUtils.class);

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_TYPE = "date";
    public static final String ATTR_TYPE = "attribute";
    public static final String LONG_TYPE = "long";
    public static final String DOUBLE_TYPE = "double";
    public static final String INTEGER_TYPE = "int";
    public static final String PRICE_TYPE = "price";
    public static final String IS_OR_NOT_KEY = "isNot";
    public static final String YES = "是";
    public static final String NO = "否";
    public static final String TYPE_SUFFIX = "_type";
    public static final String RADIO_SUFFIX = "_radio";
    public static final String SUFFIX_FLAG = "_";

    public static <T> byte[] convert2Excel(List<T> list, Class<?> T, String fieldMapStr) throws IOException,
        IntrospectionException, IllegalAccessException, IllegalArgumentException,
        InvocationTargetException {
        Assert.notNull(T);
        Assert.notNull(list);
        Assert.hasLength(fieldMapStr, "字段配置为空，请找开发配置");

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            String[] fieldMapArr = fieldMapStr.split(",");
            Map<String, String> fieldMap = new HashMap<String, String>();
            List<String> fieldSeq = new ArrayList<String>();
            for (int i = 0; i < fieldMapArr.length; ++i) {
                String[] pair = fieldMapArr[i].split(":");
                if (2 != pair.length) {
                    continue;
                }

                fieldMap.put(pair[0], pair[1]);
                fieldSeq.add(pair[0]);
            }

            Map<String, Method> readMethodMap = new HashMap<String, Method>();
            BeanInfo beanInfo = Introspector.getBeanInfo(T);
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                readMethodMap.put(descriptor.getName(), descriptor.getReadMethod());
            }

            XSSFWorkbook wwb = new XSSFWorkbook();
            XSSFSheet sheet = wwb.createSheet("导出列表");

            List<XSSFRow> rows = new ArrayList<XSSFRow>();
            for (int i = 0; i <= list.size(); ++i) {
                rows.add(sheet.createRow(i));
            }

            int i = 0;
            for (String fieldName : fieldSeq) {
                String columnName = fieldMap.get(fieldName);

                // 写入列名
                if (fieldName.contains(SUFFIX_FLAG)) {
                    continue;
                } else {
                    rows.get(0).createCell(i).setCellValue(columnName);
                }

                Method readMethod = readMethodMap.get(fieldName);

                int j = 1;
                for (T one : list) {
                    Object fieldValue = null;
                    String fieldType = fieldMap.get(fieldName + TYPE_SUFFIX);
                    Set<String> fieldTypeSet = new HashSet<String>();
                    if (null != fieldType) {
                        for (String type : fieldType.split("#")) {
                            fieldTypeSet.add(type);
                        }
                    }

                    if (null == readMethod) {
                        if (!fieldTypeSet.isEmpty()) {
                            if (fieldTypeSet.contains(ATTR_TYPE)) {
                                Method attributeReadMethod = readMethodMap.get(ATTR_TYPE);
                                String attribute = (String)attributeReadMethod.invoke(one);
                                Map<String, Object> attributeMap = JSON.parseObject(attribute);
                                fieldValue = attributeMap.get(fieldName);
                            }
                        }
                    } else {
                        fieldValue = readMethod.invoke(one);
                    }

                    if (!fieldTypeSet.isEmpty() && null != fieldValue) {
                        if (fieldTypeSet.contains(DATE_TYPE)) {
                            SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
                            fieldValue = sf.format(fieldValue);
                        }
                        if (fieldTypeSet.contains(PRICE_TYPE)) {
                            fieldValue = TypeUtils.toDouble(fieldValue) / 100;
                        }
                        if (fieldTypeSet.contains(IS_OR_NOT_KEY)) {
                            fieldValue = (1 == (Integer)fieldValue) ? YES : NO;
                        }
                    }

                    String radioArrStr = fieldMap.get(fieldName + RADIO_SUFFIX);
                    if (!StringUtils.isEmpty(radioArrStr)) {
                        if (fieldValue instanceof String) {
                            fieldValue = TypeUtils.toInteger(fieldValue);
                        }

                        String[] radioArr = radioArrStr.split("\\|\\*\\|");
                        fieldValue = radioArr[(Integer)fieldValue - 1];
                    }

                    if (null == fieldValue) {
                        rows.get(j).createCell(i).setCellValue("");
                    } else {
                        rows.get(j).createCell(i).setCellValue(fieldValue.toString());
                    }

                    ++j;
                }

                ++i;
            }

            wwb.write(bos);
            wwb.close();

            return bos.toByteArray().getBytes();
        } finally {
            if (null != bos) {
                bos.close();
            }
        }
    }

    public static <T> List<T> convert2List(InputStream is, Class<?> T, String fieldMapStr, String fileName)
        throws IntrospectionException, IOException, InstantiationException,
        IllegalAccessException, ParseException, IllegalArgumentException, InvocationTargetException {
        Assert.notNull(is);
        Assert.notNull(T);
        Assert.hasLength(fieldMapStr, "配置字段为空");
        Assert.isTrue(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"), "文件必须是xls或者xlsx格式");

        String[] fieldMapArr = fieldMapStr.split(",");
        Map<String, String> nam2FieldMap = new HashMap<String, String>();
        Map<String, String> field2NameMap = new HashMap<String, String>();
        for (int i = 0; i < fieldMapArr.length; ++i) {
            String[] pair = fieldMapArr[i].split(":");
            if (2 != pair.length) {
                continue;
            }

            nam2FieldMap.put(pair[1], pair[0]);
            field2NameMap.put(pair[0], pair[1]);
        }

        Map<String, Method> writeMethodMap = new HashMap<String, Method>();
        BeanInfo beanInfo = Introspector.getBeanInfo(T);
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            writeMethodMap.put(descriptor.getName(), descriptor.getWriteMethod());
        }

        Workbook workbook = null;
        if (fileName.endsWith(".xls")) {
            workbook = new HSSFWorkbook(is);
        } else {
            workbook = new XSSFWorkbook(is);
        }
        Sheet sheet = workbook.getSheetAt(0);
        int totalRows = sheet.getLastRowNum();
        if (totalRows > 2001) {
            throw new IOException("不要超过2000行");
        }

        int cloumnNum = 0;
        for (String fieldName : nam2FieldMap.values()) {
            if (!fieldName.contains(SUFFIX_FLAG)) {
                ++cloumnNum;
            }
        }

        Map<Integer, String> posFieldMap = new HashMap<Integer, String>();
        Row columnNames = sheet.getRow(0);
        for (int i = 0; i < cloumnNum; ++i) {
            Cell cell = columnNames.getCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);

            String content = cell.getStringCellValue();
            if (StringUtils.isEmpty(content)) {
                continue;
            }

            String fieldName = nam2FieldMap.get(content.trim());
            if (StringUtils.isEmpty(fieldName)) {
                continue;
            }

            posFieldMap.put(i, fieldName);
        }

        List<T> list = new ArrayList<T>();
        for (int i = 1; i <= totalRows; ++i) {
            // 判断空行
            boolean flag = false;
            Row curRow = sheet.getRow(i);
            for (int j = 0; j < cloumnNum; ++j) {
                Cell cell = curRow.getCell(j);
                if (cell == null) {
                    continue;
                }
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String content = cell.getStringCellValue();
                flag = flag || !StringUtils.isEmpty(content);
            }

            if (!flag) {
                continue;
            }

            @SuppressWarnings("unchecked")
            T one = (T)T.newInstance();

            Map<String, Object> attribute = new HashMap<String, Object>();
            for (int j = 0; j < cloumnNum; ++j) {
                try {
                    Cell cell = curRow.getCell(j);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String content = cell.getStringCellValue();
                    if (StringUtils.isEmpty(content)) {
                        continue;
                    }

                    content = content.trim();
                    String fieldName = posFieldMap.get(j);
                    Object fieldValue = content;
                    String fieldType = field2NameMap.get(fieldName + TYPE_SUFFIX);
                    Set<String> fieldTypeSet = new HashSet<String>();
                    if (null != fieldType) {
                        for (String type : fieldType.split("#")) {
                            fieldTypeSet.add(type);
                        }
                    }

                    String radioArrStr = field2NameMap.get(fieldName + RADIO_SUFFIX);
                    if (!StringUtils.isEmpty(radioArrStr)) {
                        int k = 1;
                        for (String radioElement : radioArrStr.split("\\|\\*\\|")) {
                            if (content.equals(radioElement)) {
                                fieldValue = String.valueOf(k);
                                break;
                            }

                            ++k;
                        }
                    }

                    if (!fieldTypeSet.isEmpty() && !StringUtils.isEmpty(content)) {
                        if (fieldTypeSet.contains(DATE_TYPE)) {
                            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                            fieldValue = sdf.parse(content);
                        }
                        if (fieldTypeSet.contains(LONG_TYPE)) {
                            fieldValue = Long.parseLong(content);
                        }
                        if (fieldTypeSet.contains(DOUBLE_TYPE)) {
                            fieldValue = Double.parseDouble(content);
                        }
                        if (fieldTypeSet.contains(INTEGER_TYPE)) {
                            fieldValue = Integer.parseInt(content);
                        }
                        if (fieldTypeSet.contains(PRICE_TYPE)) {
                            fieldValue = Double.valueOf(content).longValue() * 100;
                        }
                        if (fieldTypeSet.contains(IS_OR_NOT_KEY)) {
                            fieldValue = YES.equals(content) ? 1 : 0;
                        }
                    }

                    if (fieldTypeSet.contains(ATTR_TYPE)) {
                        attribute.put(fieldName, fieldValue);
                        continue;
                    }

                    Method m = writeMethodMap.get(fieldName);
                    if (null == m) {
                        continue;
                    }
                    m.invoke(one, fieldValue);
                } catch (Exception e) {
                    log.error(String.format("convert2List exception [msg = %s] [exception = %s]", e.getMessage(),
                        ExceptionUtils.getStackTrace(e)));
                    throw new IOException(String
                        .format("fromat error [line = %d] [column = %s]", i, field2NameMap.get(posFieldMap.get(j))));
                }
            }

            Method m = writeMethodMap.get(ATTR_TYPE);
            if (m != null) {
                m.invoke(one, JSON.toJSONString(attribute));
            }

            list.add(one);
        }

        return list;
    }

    public static void main(String[] args)
        throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
        IntrospectionException, IOException, ParseException {
        String text = "userId:商家id,data:数据,type:类型";
        System.out.println(text);
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(POIUtils.class.getClass().getResource("/").getPath()
            +"test_excel.xlsx"));
        List<RecordDO> list = convert2List(fis, RecordDO.class, text, "test_excel.xlsx");
        System.out.println(JSON.toJSONString(list));
    }
}
