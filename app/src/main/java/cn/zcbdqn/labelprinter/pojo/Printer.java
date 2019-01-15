package cn.zcbdqn.labelprinter.pojo;

import org.litepal.crud.DataSupport;

/**
 * Created by gumuyun on 2019/1/3.
 */

public class Printer extends DataSupport {
    private Integer id;
    private String printerName;
    private Integer printWidth;
    private String sn;
    private String bluetoothAddress;

    private Integer createBy;
    private String createDate;
    private Integer updateBy;
    private String updateDate;

    private String remarks;

    public Printer() {
    }

    public Printer(Integer id, String printerName, Integer printWidth, Integer createBy, String createDate) {
        this.id = id;
        this.printerName = printerName;
        this.printWidth = printWidth;
        this.createBy = createBy;
        this.createDate = createDate;
    }

    public Printer(Integer id, String printerName, Integer printWidth, String sn, String bluetoothAddress, Integer createBy, String createDate, Integer updateBy, String updateDate, String remarks) {
        this.id = id;
        this.printerName = printerName;
        this.printWidth = printWidth;
        this.sn = sn;
        this.bluetoothAddress = bluetoothAddress;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
        this.remarks = remarks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public Integer getPrintWidth() {
        return printWidth;
    }

    public void setPrintWidth(Integer printWidth) {
        this.printWidth = printWidth;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Printer{" +
                "id=" + id +
                ", printerName='" + printerName + '\'' +
                ", printWidth=" + printWidth +
                ", sn='" + sn + '\'' +
                ", bluetoothAddress='" + bluetoothAddress + '\'' +
                ", createBy=" + createBy +
                ", createDate='" + createDate + '\'' +
                ", updateBy=" + updateBy +
                ", updateDate='" + updateDate + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Printer printer = (Printer) o;

        return printerName != null ? printerName.equals(printer.printerName) : printer.printerName == null;
    }

    @Override
    public int hashCode() {
        return printerName != null ? printerName.hashCode() : 0;
    }
}
