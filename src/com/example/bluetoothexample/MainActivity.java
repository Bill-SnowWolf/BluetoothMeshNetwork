package com.example.bluetoothexample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	            foundDevices.add(device);
	            System.out.println("New Device: " + device.getName() + "\n" + device.getAddress());
	        }
	    }
	};
	
	BluetoothAdapter mBluetoothAdapter;
	ArrayAdapter<String> mArrayAdapter;
	ListView deviceListView;
	List<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();
	
	Button discoveryButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		System.out.println("On Create");
		
		// Test if Bluetooth exists
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		System.out.println("Adapter: " + mBluetoothAdapter);
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			System.out.println("Bluetooth does not support");
		} else {
			System.out.println("Bluetooth does support");
		}
		
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		
		discoveryButton = (Button)findViewById(R.id.discoveryButton);
		
		discoveryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mBluetoothAdapter.startDiscovery();
				System.out.println("Start Scannings");
				
			}
		});
		
		// Make this device discoverable forever
		Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
				startActivity(discoverableIntent);
		
		mArrayAdapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1);

		OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
		    public void onItemClick(AdapterView parent, View v, int position, long id) {
		        // Do something in response to the click
		    	System.out.println("Click " + position);
		    }
		};
		
		
		deviceListView = (ListView)findViewById(R.id.bluetoothListView);
		deviceListView.setAdapter(mArrayAdapter);
		deviceListView.setOnItemClickListener(mMessageClickedHandler);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	}
}
