package com.sample.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sample.couchbase.document.Student;

public class Application {
	
	public static void main(String[] args) {
		//createBucker();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Student student = new Student(1, "student name a", 8, "student a address");
		String json = gson.toJson(student);
		JsonObject studentJsonObject = JsonObject.fromJson(json);
		
		System.out.println("Connecting to cluster..");
		Cluster cluster = CouchbaseCluster.create();
		
		System.out.println("opening bucket....");
		Bucket bucket = cluster.openBucket("my-test-bucket", "password");
		
		System.out.println("creating student document...");
		JsonDocument studentDoc = JsonDocument.create("10001", studentJsonObject);
		
		System.out.println("Adding data to bucket....");
		JsonDocument response = bucket.upsert(studentDoc);
		
		System.out.println(response.id());
	}
	
	private static void createBucker() {
		
		Cluster cluster = CouchbaseCluster.create("127.0.0.1");
		ClusterManager clusterManager = cluster.clusterManager("Administrator", "password");
		BucketSettings bucketSettings = new DefaultBucketSettings.Builder()
		        .type(BucketType.COUCHBASE)
		        .name("my-test-bucket")
		        .password("password")
		        .quota(120)
		        .build();

		clusterManager.insertBucket(bucketSettings);
	}
}