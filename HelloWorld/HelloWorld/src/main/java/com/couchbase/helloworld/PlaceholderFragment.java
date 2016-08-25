package com.couchbase.helloworld;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    @BindView(R.id.documents)
    TextView documents;

    Database database;
    String dbname;

    public PlaceholderFragment() {
        database = Application.getDatabaseProvider().getDatabase();
        dbname = database.getName();
        writeDataToDb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showAllDocs();
    }

    private void showAllDocs() {
        try {
            Query allDocumentsQuery = database.createAllDocumentsQuery();
            allDocumentsQuery.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
            QueryEnumerator rows = allDocumentsQuery.run();
            while (rows.hasNext()) {
                QueryRow queryRow = rows.next();
                Document document = queryRow.getDocument();
                documents.setText(documents.getText() + "\r\n" +
                        document.getProperty("creationDate") + ": " +
                        document.getProperty("message"));
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    private void writeDataToDb() {
        final String TAG = "HelloWorld";
        Log.d(TAG, "Begin Hello World App");

        // get the current date and time
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());

        // create an object that contains data for a document
        Map<String, Object> docContent = new HashMap<>();
        docContent.put("message", "Hello Couchbase Lite");
        docContent.put("creationDate", currentTimeString);

        // display the data for the new document
        Log.d(TAG, "docContent=" + String.valueOf(docContent));

        // create an empty document
        Document document = database.createDocument();

        // add content to document and write the document to the database
        try {
            document.putProperties(docContent);
            Log.d(TAG, "Document written to database named " + dbname + " with ID = " + document.getId());
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot write document to database", e);
        }

        // save the ID of the new document
        String docID = document.getId();

        // retrieve the document from the database
        Document retrievedDocument = database.getDocument(docID);

        // display the retrieved document
        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
//        documents.setText(retrievedDocument.toString());

//        // update the document
//        Map<String, Object> updatedProperties = new HashMap<>();
//        updatedProperties.putAll(retrievedDocument.getProperties());
//        updatedProperties.put("message", "We're having a heat wave!");
//        updatedProperties.put("temperature", "95");
//
//        try {
//            retrievedDocument.putProperties(updatedProperties);
//            Log.d(TAG, "updated retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
//        } catch (CouchbaseLiteException e) {
//            Log.e(TAG, "Cannot update document", e);
//        }

//        // delete the document
//        try {
//            retrievedDocument.delete();
//            Log.d(TAG, "Deleted document, deletion status = " + retrievedDocument.isDeleted());
//        } catch (CouchbaseLiteException e) {
//            Log.e(TAG, "Cannot delete document", e);
//        }

    }
}
