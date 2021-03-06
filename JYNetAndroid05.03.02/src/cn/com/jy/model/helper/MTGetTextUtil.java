package cn.com.jy.model.helper;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MTGetTextUtil {
	public static String getText(Button v){
		String string;
		try {
			string=v.getText().toString();
			if(string.equals("")||!string.contains("年")){
				string="未填";
			}
		} catch (Exception e) {
			string="未填";
		}
		return string;
	}
	public static String getText(TextView v){
		String string;
		try {
			string=v.getText().toString();
			if(string.equals("")){
				string="0";
			}
		} catch (Exception e) {
			string="0";
		}
		return string;
	}
	public static String getText(EditText v){
		String string;
		try {
			string=v.getText().toString();
			if(string.equals("")){
				string="0";
			}
		} catch (Exception e) {
			string="0";
		}
		return string;
	}
	public static String getText(Spinner v){
		String string;
		try {
			string=v.getSelectedItem().toString();
		} catch (Exception e) {
			string="0";
		}
		return string;
	}
}
