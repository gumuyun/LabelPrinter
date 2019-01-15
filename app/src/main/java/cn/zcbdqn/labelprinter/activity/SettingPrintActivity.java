package cn.zcbdqn.labelprinter.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.context.MyApplication;
import cn.zcbdqn.labelprinter.util.SharedPreferencesUtil;
import cn.zcbdqn.labelprinter.util.StatusBox;
import cn.zcbdqn.labelprinter.util.ZKPrintUtils;

public class SettingPrintActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /**
     * 蓝牙打印机列表显示
     */
    private ListView blueToothListView;
    /**
     * 返回按扭
     */
    private ImageView backBtn;
    /**
     * 蓝牙适配器-android自带
     */
    private static BluetoothAdapter myBluetoothAdapter;
    private StatusBox statusBox;
    /**
     * 黙认的打印机地址
     */
    private String defaultBluePrintAddress;
    /**
     * 适配器,显示蓝牙设备
     */
    private SimpleAdapter adapter;
    /**
     * 蓝牙设备
     */
    private List<Map<String, String>> list;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        setContentView(R.layout.activity_setting_print);
        //获得已配置的黙认蓝牙地址
        defaultBluePrintAddress=(String) MyApplication.applicationMap.get("defaultBluePrintAddress");
        //创建轻量级的存储对象
        Log.i("device", "MyApplication中蓝牙地址:"+defaultBluePrintAddress);
        //注册View
        blueToothListView=(ListView) findViewById(R.id.setting_print_lv);
        backBtn=(ImageView) findViewById(R.id.back);

        //设置监听事件
        backBtn.setOnClickListener(this);
        //设置ListView的项点击事件
        blueToothListView.setOnItemClickListener(this);
        //获得蓝牙设备并显示
        getBluetoothDevice();
    }
    /**
     * 获得系统配置的蓝牙设备,剔除非打印机,并显示
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean getBluetoothDevice() {
        list = new ArrayList<Map<String, String>>();
        adapter = new SimpleAdapter(this, list,
                R.layout.item_setting_print,
                new String[]{"printName","printAddress","default"},
                new int[]{R.id.item_setting_print_name,R.id.item_setting_print_address,R.id.item_setting_print_default}
        );
        blueToothListView.setAdapter(adapter);

        //如果没有找到蓝牙适配器,提示
        if ((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) == null) {
            Toast.makeText(this, "没有找到蓝牙适配器", Toast.LENGTH_LONG).show();
            return false;
        }
        //如果蓝牙未开启,跳转开启
        if (!myBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 2);
        }

        //获得已配置的蓝牙
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() <= 0){
            return false;
        }
        for (BluetoothDevice device : pairedDevices) {

            BluetoothClass bluetoothClass = device.getBluetoothClass();
            Log.i("device", bluetoothClass.toString());
            Log.i("device", bluetoothClass.getDeviceClass()+"----DeviceClass");

            //过滤非蓝牙打印机
            if(bluetoothClass.getDeviceClass()!=1664){
                continue;
            }
            //获得蓝牙设备名称和蓝牙地址
            Map<String, String> map = new HashMap<String, String>();
            String btAddress = device.getAddress();
            if(btAddress.equals(defaultBluePrintAddress)){
                map.put("default", "黙认");
            }else{
                map.put("default", "");
            }
            //Toast.makeText(this, "蓝牙名称:"+device.getName()+":"+btAddress+"---类型:"+device.getType(), Toast.LENGTH_SHORT).show()
            Log.i("device", "蓝牙名称:"+device.getName()+":"+btAddress+"---类型:"+device.getType());
            map.put("printName", "打印机名称:"+device.getName());
            map.put("printAddress", "蓝牙地址:"+device.getAddress());
            map.put("address", device.getAddress());
            list.add(map);
        }

        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //选择某项的时候
        //获得蓝牙地址
        //设置到MyApplication中
        Map<String,String> map = (Map<String, String>) parent.getItemAtPosition(position);
        defaultBluePrintAddress=map.get("address");
        MyApplication.applicationMap.put("defaultBluePrintAddress", defaultBluePrintAddress);
        //其它的清除黙认选中
        for (Map<String,String> dataMap : list) {
            dataMap.put("default", "");
        }
        map.put("default", "黙认");
        adapter.notifyDataSetChanged();

        //保存至文件中
        SharedPreferencesUtil.putString("defaultBluePrintAddress",defaultBluePrintAddress);

        //测式打印机
        Log.i("device", defaultBluePrintAddress);
        //Toast.makeText(this, defaultBluePrintAddress, Toast.LENGTH_LONG).show();
        showDialog();
        new Thread(){
            public void run() {
                int status= ZKPrintUtils.getStatus(SettingPrintActivity.this, defaultBluePrintAddress);
                handler.sendEmptyMessage(status);
            };
        }.start();
        //ZKPrintUtils.print(defaultBluePrintAddress, "http:www.baid.com", true, "http:www.baid.com", true,0, this);
        currentTime=System.currentTimeMillis();
    }

    ProgressDialog progressDialog;

    public void showDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在查询打印机状态...");
        progressDialog.setMessage("请稍后...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    Handler handler=new Handler(){

        public void handleMessage(Message msg) {

            progressDialog.dismiss();
            switch (msg.what) {
                case -1:
                    Toast.makeText(SettingPrintActivity.this, "连接打印机失败", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(SettingPrintActivity.this, "打印机正常", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(SettingPrintActivity.this, "缺纸", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(SettingPrintActivity.this, "纸仓未合上", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(SettingPrintActivity.this, "获取状态异常", Toast.LENGTH_LONG).show();
                    break;
            }

        };
    };

    long currentTime=0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                //点击返回,跳转至主设置界面
                Intent intent=new Intent();
                intent.putExtra("resultCode", 1);
                //Toast.makeText(this, "click back", Toast.LENGTH_LONG).show();
                setResult(1,intent);
                finish();
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        MyApplication.getInstance().finishActivity(this);
        super.onDestroy();
    }

/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting_print, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
}
