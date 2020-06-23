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

package com.google.sps;
import java.util.Comparator;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
      ArrayList<TimeRange> requiredAttendees = new ArrayList<TimeRange>();
      ArrayList<TimeRange> busyTimeSlots = new ArrayList<TimeRange>();
      //Get TimeRange of all attendees required for the meeting request
      for (Event event : events) {
          for (String e : event.getAttendees()) {
              if (request.getAttendees().contains(e)) {
                  requiredAttendees.add(event.getWhen());
              }
          }
      }
      //Error handling for 3 situations:
      //1) Handle situations when all required attendees are free
      //2) Handle situations when all required attendees are free but requested duration is longer than a day
      //3) When the request does not specify any attendee
      if (requiredAttendees.size()==0) {
          if (request.getDuration()<1440) {
            requiredAttendees.add(TimeRange.fromStartDuration(0,1440));
          }
          return requiredAttendees;
      }

      // Sort TimeRange by start time
      Collections.sort(requiredAttendees,TimeRange.ORDER_BY_START);

      //
      busyTimeSlots.add(requiredAttendees.get(0));
      // Loop through the busy Timerange of every attendee, starting from second attendee
      // compares start time of attendee B with end time of attendee A (after sorting by start time)
      // If start time of B <= end time of A && end time of B > end time of A:
        //Set or replace the last index of busyTimeSlots arraylist with a new TimeRange whereby
        // the new start time is start time of attendee A and the new end time is end time of attendee B
      for (int i=1; i < requiredAttendees.size(); i++) {
          if (requiredAttendees.get(i).start() <= busyTimeSlots.get(busyTimeSlots.size()-1).end()) {
              if (requiredAttendees.get(i).end() > busyTimeSlots.get(busyTimeSlots.size()-1).end()) {
                  TimeRange tmp = busyTimeSlots.get(busyTimeSlots.size()-1);
                  busyTimeSlots.set(busyTimeSlots.size()-1, tmp.fromStartEnd(tmp.start(),requiredAttendees.get(i).end(),false));
              }
          } else {
              busyTimeSlots.add(requiredAttendees.get(i));
          }
      }

      //Adds TimeRange object for when the first object does not start at timing=0
      if (busyTimeSlots.get(0).start() != 0) {
          TimeRange tmp = busyTimeSlots.get(0);
          busyTimeSlots.add(0,tmp.fromStartEnd(0,0,false));
      }

      //Adds TimeRange object for when the last object does not end at timing=1440
      if (busyTimeSlots.get(busyTimeSlots.size()-1).end() != 1440) {
          TimeRange tmp = busyTimeSlots.get(busyTimeSlots.size()-1);
          busyTimeSlots.add(tmp.fromStartEnd(1440,1440,true));
      }

      //Based on the busyTimeSlots of attendees, determine pockets of available time
      //whereby I can schedule the requested meeting
      ArrayList<TimeRange> availTimeSlots = new ArrayList<TimeRange>();
      
      for (int i=1;i<busyTimeSlots.size();i++) {
          if (busyTimeSlots.get(i).start() - busyTimeSlots.get(i-1).end() >= request.getDuration()) {
            TimeRange tmp = busyTimeSlots.get(i);
            int tmp1 = busyTimeSlots.get(i).start();
            int tmp2 = busyTimeSlots.get(i-1).end();

            availTimeSlots.add(tmp.fromStartEnd(tmp2,tmp1,false));
          }
      }
      return availTimeSlots;
  }
}
