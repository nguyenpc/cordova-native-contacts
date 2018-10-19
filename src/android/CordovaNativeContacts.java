package cordova.plugin.contact;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.net.Uri;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import cordova.plugin.contact.FileProvider;

public class CordovaNativeContacts extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("addContact")) {
            Context context = this.cordova.getActivity().getApplication();
            Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
            JSONObject params = args.getJSONObject(0);
            addExtra(intent, params, "name", ContactsContract.Intents.Insert.NAME);
            addExtra(intent, params, "phone", ContactsContract.Intents.Insert.PHONE);
            addExtra(intent, params, "email", ContactsContract.Intents.Insert.EMAIL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("finishActivityOnSaveCompleted", true);
            context.startActivity(intent);
            return true;
        }
         if (action.equals("updateContact")) {
            Context context = this.cordova.getActivity().getApplication();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // intent.setType(Contacts.CONTENT_VCARD_TYPE);
            JSONObject params = args.getJSONObject(0);

            File file = new File(this.stripFileProtocol(params.getString("path")));
						Uri path = FileProvider.getUriForFile(context, cordova.getActivity().getPackageName() + ".opener.provider", file);

            intent.setDataAndType(path,"text/vcard");

            // addExtra(intent, params, "name", ContactsContract.Intents.Insert.NAME);
            // addExtra(intent, params, "phone", ContactsContract.Intents.Insert.PHONE);
            // addExtra(intent, params, "email", ContactsContract.Intents.Insert.EMAIL);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);

            intent.putExtra("finishActivityOnSaveCompleted", true);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    private void addExtra(Intent intent, JSONObject params, String paramKey, String extraKey) throws JSONException {
        String value = params.getString(paramKey);
        if (value != null) {
            intent.putExtra(extraKey, value);
        }
    }

    private String stripFileProtocol(String uriString) {
		if (uriString.startsWith("file://")) {
			uriString = uriString.substring(7);
		} else if (uriString.startsWith("content://")) {
			uriString = uriString.substring(10);
		}
		return uriString;
	}
}
