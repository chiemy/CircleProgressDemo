package com.example.circleprogressdemo;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnClickListener,OnSeekBarChangeListener{
	Button btn1,btn2;
	CircleProgress circlePro;
	SeekBar seekBar,radioBar,progressWidthSeekBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		radioBar = (SeekBar) findViewById(R.id.seekBar2);
		progressWidthSeekBar = (SeekBar) findViewById(R.id.seekBar3);
		circlePro = (CircleProgress) findViewById(R.id.circleProgress1);
		seekBar.setOnSeekBarChangeListener(this);
		radioBar.setOnSeekBarChangeListener(this);
		progressWidthSeekBar.setOnSeekBarChangeListener(this);
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			circlePro.setVisibility(View.VISIBLE);
			break;
		case R.id.button2:
			circlePro.setVisibility(View.GONE);
			break;
		}
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch(seekBar.getId()){
		case R.id.seekBar1:
			circlePro.setProgress(progress);
			break;
		case R.id.seekBar2:
			circlePro.setRaduio(50 + (int)(progress*0.5));
			break;
		case R.id.seekBar3:
			circlePro.setProgressWidth(10 + (int)(0.2*progress));
			break;
		}
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}


}
