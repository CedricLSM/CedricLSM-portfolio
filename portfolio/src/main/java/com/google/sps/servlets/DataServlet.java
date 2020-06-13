// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private ArrayList<String> history = new ArrayList<String>();


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // String json = convertToJson(messages);
    // response.setContentType("text/html;");
    // response.getWriter().println("<h1>Hello Cedric!</h1>");
    // String json = convertToJson(history);

    // New Query instance to load Comment entity
    Query query = new Query("Comment");

    // Initialise datastore and pass query into datastore.prepare() function
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Loop over entities using asIterable() function
    List<String> hist = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String comment = (String) entity.getProperty("comment");
      hist.add(comment);
    }

    response.setContentType("application/json;");
    String json = new Gson().toJson(tasks);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String userComment = request.getParameter("user-comment").trim();

    // Input validation.
    if (userComment.length() <= 3) {
      response.setContentType("text/html");
      response.getWriter().println("Please enter a comment that is non-empty and longer than 3 characters.");
      return;
    }

    // Store Comment entity
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment", userComment);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    history.add(userComment);

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }

  /**
   * Converts a ServerStats instance into a JSON string using manual String concatentation.
   */
    private String convertToJson(ArrayList<String> history) {
        String json = "{";
        json += "\"history\": ";
        json += "\"" + history + "\"";
        // json += ", ";
        // json += "\"message_2\": ";
        // json += "\"" + messages.get(1) + "\"";
        // json += ", ";
        // json += "\"message_3\": ";
        // json += "\"" + messages.get(2) + "\"";
        json += "}";
        return json;
  }

}
