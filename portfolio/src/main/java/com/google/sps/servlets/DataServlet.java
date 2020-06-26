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
  private ArrayList<String> messages;

  @Override
  public void init() {
    messages = new ArrayList<String>();
    messages.add("A");
    messages.add("B");
    messages.add("C");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertToJson(messages);
    // response.setContentType("text/html;");
    // response.getWriter().println("<h1>Hello Cedric!</h1>");

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
   * Converts a ServerStats instance into a JSON string using manual String concatentation.
   */
    private String convertToJson(ArrayList<String> messages) {
        String json = "{";
        json += "\"message_1\": ";
        json += "\"" + messages.get(0) + "\"";
        json += ", ";
        json += "\"message_2\": ";
        json += "\"" + messages.get(1) + "\"";
        json += ", ";
        json += "\"message_3\": ";
        json += "\"" + messages.get(2) + "\"";
        json += "}";
        return json;
  }

}
