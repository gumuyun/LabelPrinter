package cn.zcbdqn.labelprinter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.pojo.Printer;

public class SelectPrinterAdapter extends BaseAdapter {

	private List<Printer> list;
	private LayoutInflater inflater;



	public List<Printer> getList() {
		return list;
	}

	public void setList(List<Printer> list) {
		this.list = list;
	}

	public SelectPrinterAdapter(Context context) {
		inflater= LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list==null?0:list.size();
	}

	@Override
	public Object getItem(int position) {
		return list==null?null:list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=inflater.inflate(R.layout.item_select_printer, null);
			holder.printerNameTv=(TextView) convertView.findViewById(R.id.item_warehouse_name);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		Printer printer = list.get(position);
		holder.printerNameTv.setText(printer.getPrinterName());
		return convertView;
	}

	private static class Holder{
		TextView printerNameTv;
	}
}
