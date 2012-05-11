package com.delegator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;

public class DirectIO {
	
	static String file; // = ContextWrapper.getFilesDir(); // new File(Environment.getDataDirectory(), "/data.json"); // getDataDirectory() + "data.json";
	
	public static void jsonSetContext(Context c){
		file = c.getFilesDir() + "/data.json";
	}
	
	/**
	 * First Check, onCreate(), 
	 * if File does not exist,
	 * then create a standard file with a categoryitem.  
	 * 
	 * @author NoNaMeNo
	 */
	public static void checkFirst(){
		if(File2JSON() == null){
			Log.w("checkFirst", "File does not exist.");
			CategoryItem currentCategory = new CategoryItem("General");
			JSONObject json = new JSONObject();
            try {
				json.put("items", new JSONArray().put(item2JSON(currentCategory)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
            JSON2File(json.toString());
            Log.w("checkFirst", "It does now: " + json.toString());
		} else {
			Log.w("checkFirst", "File exists: " + File2JSON().toString());		
		}
	}
	
	/**
	 * Add an item and save to File.
	 * if Item = Category 
	 * then add at last position,
	 * else look for Item's category and put it as first task after the category 
	 * fi
	 * 
	 * @param item
	 * 
	 * @author NoNaMeNo
	 */
	public static void NewItem(Item item){
		
		ArrayList<Item> items = DelegatorActivity.items;
		if(item.isCategory()){
			Log.w("NewItem", "Item is Category.");
			items.add(item);
		} else {
			Log.w("NewItem", "Item is Task.");
			for(int i = 0; i<items.size(); i++){
				if(items.get(i).isCategory()){
					if(((CategoryItem) (items.get(i))).title.equals(((Task) item).category)){
						items.add(i+1, item);
					}
				}
			}
		}
		
		Log.w("NewItem", "Item added.");
		try {
			ItemList2File(items);
			Log.w("NewItem", "Item saved.");
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w("NewItem", "Item could not be saved.");
		}
	}
	
	/**
	 * Update an item and save to File.
	 * The item gets updated removing the old one and adding the new one 
	 * in the same index.
	 * 
	 * @param item (only tasks)
	 * 
	 * @author NoNaMeNo
	 */
	public static void UpdateItem(Item item){
		
		ArrayList<Item> items = DelegatorActivity.items;
		
		int index = items.indexOf(item);
		
		Log.w("UpdateItem", "Item is Task.");
		items.remove(item);
		items.add(index, item);
		Log.w("UpdateItem", "Item updated.");
		try {
			ItemList2File(items);
			Log.w("UpdateItem", "Item saved.");
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w("UpdateItem", "Item could not be saved.");
		}
	}
	
	/**
	 * Remove an item and save to File.
	 * if Item = CategoryItem,
	 * then if item = last or item position +1 a CategoryItem, then remove item,
	 * else remove item,
	 * fi
	 * 
	 * @param item
	 * @return false if category is not empty, true if success
	 * @author NoNaMeNo
	 */
	public static boolean RemoveItem(Item item){
		
		ArrayList<Item> items = DelegatorActivity.items;
		
		int index = items.indexOf(item);
		
		if(item.isCategory()){
			Log.w("RemoveItem", "Item is Category.");
			if(index == items.size() -1){
				items.remove(item);
			} else if(items.get(index+1).isCategory()){
				items.remove(item);
			} else {
				return false;
			}
		} else {
			Log.w("RemoveItem", "Item is Task.");
			items.remove(item);
		}
		
		Log.w("RemoveItem", "Item removed.");
		try {
			ItemList2File(items);
			Log.w("RemoveItem", "Item saved.");
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w("RemoveItem", "Item could not be saved.");
		}
		
		return true;
	}
	
	/**
	 * Serializes a item into JSON
	 * 
	 * @param item
	 * @return json object
	 * 
	 * @author NoNaMeNo
	 */
	private static JSONObject item2JSON(Item item){
		Gson gson = new Gson();
		JSONObject json = null;
		try {
			json = new JSONObject(gson.toJson(item));
			Log.w("item2JSON", "Item to JSON Succeded: " + json);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w("item2JSON", "Item to JSON Failed.");
		}
		return json;
	}
	
	/**
	 * Writes the file with the Item array
	 * @param item array
	 * @throws JSONException
	 * 
	 * @author NoNaMeNo
	 */
	private static void ItemList2File(ArrayList<Item> items) throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < items.size(); i++){
			jsonArray.put(item2JSON(items.get(i)));
		}
		json.put("items", jsonArray);
		Log.w("ItemList2File", "List done: " + json);
		JSON2File(json.toString());
	}
	
	/**
	 * Reads the file and returns the Item array
	 * 
	 * @return item array
	 * @throws JSONException
	 * 
	 * @author NoNaMeNo
	 */
	public static ArrayList<Item> File2ItemList() throws JSONException {
		ArrayList<Item> itemToReturn = new ArrayList<Item>();
		Item itemToList;
		JSONObject jsonTree = File2JSON();
		JSONArray jsonListItems = jsonTree.getJSONArray("items");
		Gson gson = new Gson();
		
		for (int i = 0; i < jsonListItems.length(); i++) {
			JSONObject item = jsonListItems.getJSONObject(i);
			if(item.getBoolean("isCategory")){
				itemToList = (Item) gson.fromJson(item.toString(), CategoryItem.class);
			} else {
				itemToList = (Item) gson.fromJson(item.toString(), Task.class);
			}
			itemToReturn.add(itemToList);
		} 
		return itemToReturn;
	}
	
	/**
	 * Saves a JSON String to File.
	 * 
	 * @param json
	 * @author NoNaMeNo
	 */
	private static void JSON2File(String json){
		try {
			FileWriter fileOut = new FileWriter(file);
			fileOut.write(json.toString());
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads File and returns a parsed string into a JSONObject.
	 * @return a parsed string into a JSONObject
	 * 
	 * @author NoNaMeNo
	 */
	private static JSONObject File2JSON() {
		try {
			FileReader fileIn = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileIn);
			String line = reader.readLine();
			JSONObject jsonObject = new JSONObject(line);
			fileIn.close();
			return jsonObject;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
