package com.example.ya_threadandhandlertest2;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private Button btn;
	private ImageView iv;
	private String imgPath = 
			"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzULvOQ1cfurAJowVB3JsT1aQKcPrnogwl0RV0so-Akzc-1v8zBA";
	private static final int DOWNLOAD_IMG = 1;
	private ProgressDialog dialog = null;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(android.os.Message msg) {
			byte[] data = (byte[])msg.obj;
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			iv.setImageBitmap(bitmap);
			if(msg.what == DOWNLOAD_IMG){
				dialog.dismiss();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComponent();
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				new Thread(new MyThread()).start();
				dialog.show();
			}
		});
	}
	
	private void initComponent(){
		btn = (Button)findViewById(R.id.button1);
		iv = (ImageView)findViewById(R.id.imageView1);
		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在下載中...");
		dialog.setCancelable(false);
	}
	
	public class MyThread implements Runnable{

		@Override
		public void run() {
			HttpClient hc = new DefaultHttpClient();
			HttpGet hg = new HttpGet(imgPath);
			HttpResponse hr = null;
			try{
				hr = hc.execute(hg);
				if(200 == hr.getStatusLine().getStatusCode()){
					byte[] data = EntityUtils.toByteArray(hr.getEntity());
					
					Message message = Message.obtain();
					message.obj = data;
					message.what = DOWNLOAD_IMG;
					handler.sendMessage(message);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	
	
	
	
}
