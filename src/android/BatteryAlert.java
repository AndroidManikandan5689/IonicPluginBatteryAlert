package cordova.plugin.raqmiyat.battery;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.BATTERY_SERVICE;

/**
 * This class echoes a string called from JavaScript.
 */
public class BatteryAlert extends CordovaPlugin {

    private static final String TAG = "BatteryAlert";

    // Action for battery alert
    public static final String ACTION_BATTERY = "batteryAlert";

    // Intialize the broadcase receiver for batter status change
    private BroadcastReceiver receiver;
    // Battery callback context from cordova
    private CallbackContext callbackContext = null;

    // Constructor
    public BatteryAlert() {
        this.receiver = null;
    }    


    // Execution method
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        try {
            String isAlertShow = args.getString(0);
            if (ACTION_BATTERY.equals(action)) {
                if (this.callbackContext != null) {
                    unRegisterListener();
                }
                else {
                    this.callbackContext = callbackContext;
                }

                if (this.receiver == null) {
                    this.receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent batteryIntent) {
                            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                            int level = batteryIntent != null ? batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
                            int scale = batteryIntent != null ? batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
                            Integer batteryPercentage = calculatePercentage(level, scale);

                            //Show alert only device battery will go below 10 percentage
                            if(batteryPercentage <= 10) {
                                showAlert(batteryPercentage.toString());
                            }
                            sendResult(batteryPercentage.toString());
                            callbackContext.success("success");

                        }
                    };


                    // Get bettery level
                    Integer batteryLevel = getBatteryPercentage();
                    if(isAlertShow.equals("1"))
                    {
                    showAlert(batteryLevel.toString());
                    }
                    else {
                        Toast.makeText(this.cordova.getContext(),"Device Battery Percentage " + batteryLevel + " %", Toast.LENGTH_SHORT ).show();
                    }
                    callbackContext.success("success");

                }

            } else {
                callbackContext.error("wrong method");
                return false;
            }
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
            return false;
        }
        return false;
    }


    // Method for getting battery percentage
    public int getBatteryPercentage() {
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = this.cordova.getActivity().registerReceiver(this.receiver, iFilter);
            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
            return calculatePercentage(level, scale);
    }


    // Calculate the percentage value using level and scale from broadcast receiver
    public int calculatePercentage(int level, int scale)
    {
        double batteryPct = level / (double) scale;
        return (int) (batteryPct * 100);
    }


    // Send result to cordova application
    private void sendResult(String value) {
        if (this.callbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, value);
            result.setKeepCallback(true);
            this.callbackContext.sendPluginResult(result);
        }
    }

    // Show native alert dialog
    private void showAlert(String level) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.cordova.getActivity());
        dialog.setTitle("Battery Status");
        dialog.setMessage("Device Battery Percentage " + level + " %");
        dialog.show();
    }

    public void onDestroy() {
        unRegisterListener();
    }

    private void unRegisterListener() {
        if (this.receiver != null) {
            try {
                webView.getContext().unregisterReceiver(this.receiver);
                this.receiver = null;
            } catch (Exception e) {
                LOG.e(TAG, "Error unregistering the receiver: " + e.getMessage(), e);
            }
        }
    }

}
