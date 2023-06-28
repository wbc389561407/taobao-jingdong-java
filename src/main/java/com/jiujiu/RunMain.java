package com.jiujiu;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author 389561407@qq.com
 * @version 1.0
 * @since 2022-11-17
 */
public class RunMain {
    private JPanel root;
    private JLabel filePath;
    private JTextField textField1;
    private JButton fileb;

    private JLabel status;
    private JLabel stautsShow;
    private JLabel runState;
    private JLabel vs;
    private JLabel vsText;
    private JLabel runStateText;
    private JLabel title;
    private JButton tbButton;
    private JButton jdButton;


    public RunMain() {

        vsText.setText(PropertiesUtil.getValue("vs"));


        new DropTarget(textField1, DnDConstants.ACTION_COPY_OR_MOVE,
                new DropTargetAdapter() {
                    @Override
                    public void drop(DropTargetDropEvent dtde) {
                        try {
                            // 如果拖入的文件格式受支持
                            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                                System.out.println("接受");
//                                // 接收拖拽来的数据
                                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                                @SuppressWarnings("unchecked")
                                List<File> list = (List<File>) (dtde.getTransferable()
                                        .getTransferData(DataFlavor.javaFileListFlavor));
                                //area.append("");
                                File[] files = new File[1];
                                files[0] = list.get(0);
                                System.out.println("添加文件：" + list.get(0).getAbsolutePath());
                                textField1.setText(list.get(0).getAbsolutePath());

                                // 指示拖拽操作已完成
                                dtde.dropComplete(true);
                                System.out.println("拖拽完成");
                            } else {
                                // 拒绝拖拽来的数据
                                dtde.rejectDrop();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });


        tbButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    runStateText.setText("正在运行");
                    String path = textField1.getText();
                    System.out.println(path);
                    System.out.println("淘宝");
                    int i = path.lastIndexOf('\\');
                    String substring = path.substring(0, i);
                    String filePath = substring + "/淘宝" + System.currentTimeMillis() + ".xls";
                    ExcelUtil excelUtil1 = new ExcelUtil(filePath);
                    String[] title = {"关键词", "搜索关键词", "商品名称", "店铺名称", "商品价格", "商品销量", "商品图片", "店铺链接"};
                    // 把 title 转换为 list
                    List<String> titleList = Arrays.asList(title);
                    System.out.println(excelUtil1.getAllRowCount());
                    if (excelUtil1.getAllRowCount() > 0) {
                        excelUtil1.setOneRowData(titleList, 0);
                    }
                    excelUtil1.writeAndClose();
                    //读取文件内容 返回一个字符串数组
                    ExcelUtil excelUtil = new ExcelUtil(path);
                    //第二行开始读取
                    List<String> oneColumnData = excelUtil.getOneColumnData(1);
                    oneColumnData.remove(0);

                    //遍历字符串数组, 查询淘宝第一页 返回数据 写入 Excel
                    for (String s : oneColumnData) {
                        ExcelUtil excelUtil2 = new ExcelUtil(filePath);

                        System.out.println("开始查询关键词:" + s);
                        stautsShow.setText("正在查询关键词:" + s);
                        //查询后写入 Excel
                        try {
                            TaobaoUtil.getTbData(s, excelUtil2);
                        } catch (Exception e1) {
                            System.out.println("输入 cookie 有误 请重新输入");
                            //java 弹窗输入
                            TaobaoUtil.cookie = JOptionPane.showInputDialog("输入 cookie");
                            TaobaoUtil.getTbData(s, excelUtil1);
                        }
                        excelUtil2.writeAndClose();
                        //写入 Excel
                        System.out.println("暂停 观察数据 测试");
                        try {
                            Thread.sleep(1000 * 10);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    runStateText.setText("未运行");
                    stautsShow.setText("搜索完成");
                }).start();

            }
        });
        jdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("京东");
                new Thread(() -> {
                    runStateText.setText("正在运行");
                    String path = textField1.getText();
                    System.out.println(path);
                    int i = path.lastIndexOf('\\');
                    String substring = path.substring(0, i);
                    String filePath = substring + "/京东" + System.currentTimeMillis() + ".xls";
                    ExcelUtil excelUtil1 = new ExcelUtil(filePath);
                    String[] title = {"关键词", "搜索关键词", "商品名称", "店铺名称", "商品价格", "商品销量", "商品图片", "店铺链接"};
                    // 把 title 转换为 list
                    List<String> titleList = Arrays.asList(title);
                    System.out.println(excelUtil1.getAllRowCount());
                    if (excelUtil1.getAllRowCount() > 0) {
                        excelUtil1.setOneRowData(titleList, 0);
                    }
                    excelUtil1.writeAndClose();
                    //读取文件内容 返回一个字符串数组
                    ExcelUtil excelUtil = new ExcelUtil(path);
                    //第二行开始读取
                    List<String> oneColumnData = excelUtil.getOneColumnData(1);
                    oneColumnData.remove(0);

                    //遍历字符串数组, 查询淘宝第一页 返回数据 写入 Excel
                    for (String s : oneColumnData) {
                        ExcelUtil excelUtil2 = new ExcelUtil(filePath);

                        System.out.println("开始查询关键词:" + s);
                        stautsShow.setText("正在查询关键词:" + s);
                        //查询后写入 Excel
                        JdUtil.getData(s, excelUtil2);

                        excelUtil2.writeAndClose();
                        //写入 Excel
                        System.out.println("暂停 观察数据 测试");
                        try {
                            Thread.sleep(1000 * 10);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    runStateText.setText("未运行");
                    stautsShow.setText("搜索完成");
                }).start();
                //弹出提示 正在开发中
//                JOptionPane.showMessageDialog(null, "正在开发中");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RunMain");
        frame.setContentPane(new RunMain().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setIconImage(Toolkit.getDefaultToolkit().createImage("icon/simple.png"));

        frame.setSize(700, 360);
        frame.setLocationRelativeTo(null);//居中显示
        frame.setVisible(true);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        root.setBackground(new Color(-11611918));
        filePath = new JLabel();
        filePath.setBackground(new Color(-857102));
        Font filePathFont = this.$$$getFont$$$(null, Font.BOLD, 18, filePath.getFont());
        if (filePathFont != null) filePath.setFont(filePathFont);
        filePath.setHorizontalAlignment(2);
        filePath.setText("文件路径：");
        root.add(filePath, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        Font textField1Font = this.$$$getFont$$$(null, Font.BOLD, 18, textField1.getFont());
        if (textField1Font != null) textField1.setFont(textField1Font);
        textField1.setText("将文件或者文件夹拖入此处");
        root.add(textField1, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fileb = new JButton();
        Font filebFont = this.$$$getFont$$$(null, Font.BOLD, 18, fileb.getFont());
        if (filebFont != null) fileb.setFont(filebFont);
        fileb.setText("选择文件");
        root.add(fileb, new GridConstraints(2, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stautsShow = new JLabel();
        Font stautsShowFont = this.$$$getFont$$$(null, Font.BOLD, 20, stautsShow.getFont());
        if (stautsShowFont != null) stautsShow.setFont(stautsShowFont);
        stautsShow.setText("");
        stautsShow.setToolTipText("");
        root.add(stautsShow, new GridConstraints(3, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        status = new JLabel();
        Font statusFont = this.$$$getFont$$$(null, Font.BOLD, 20, status.getFont());
        if (statusFont != null) status.setFont(statusFont);
        status.setHorizontalAlignment(2);
        status.setText("运行报告：");
        root.add(status, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vs = new JLabel();
        vs.setText("版本：");
        root.add(vs, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vsText = new JLabel();
        vsText.setText("");
        root.add(vsText, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        runState = new JLabel();
        runState.setText("状态：");
        root.add(runState, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        runStateText = new JLabel();
        runStateText.setText("未运行");
        root.add(runStateText, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        title = new JLabel();
        Font titleFont = this.$$$getFont$$$(null, Font.BOLD, 36, title.getFont());
        if (titleFont != null) title.setFont(titleFont);
        title.setText("价格搜索工具");
        root.add(title, new GridConstraints(0, 0, 2, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tbButton = new JButton();
        tbButton.setText("淘宝");
        root.add(tbButton, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jdButton = new JButton();
        jdButton.setText("京东");
        root.add(jdButton, new GridConstraints(4, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}
