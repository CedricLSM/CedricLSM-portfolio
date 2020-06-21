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
      // Sort TimeRange by start time
      Collections.sort(requiredAttendees,TimeRange.ORDER_BY_START);

      //
      busyTimeSlots.add(requiredAttendees.get(0));
      // Loop through the busy Timerange of every attendee, starting from second attendee
      // compares start time of attendee B with end of attendee A (after sorting by start time)
      // If start time of B <= end time of A && end time of B > end time of A:
        //Set or replace the last index of busyTimeSlots arraylist with a new TimeRange whereby
        // the new start time is start time of attendee A and the new end time is end time of attendee B
      for (int i=1; i < requiredAttendees.size(); i++) {
          if (requiredAttendees.get(i).start() <= busyTimeSlots.get(busyTimeSlots.size()-1).end()) {
              if (requiredAttendees.get(i).end() > busyTimeSlots.get(busyTimeSlots.size()-1).end()) {
                  TimeRange tmp = busyTimeSlots.get(busyTimeSlots.size()-1);
                  busyTimeSlots.set(busyTimeSlots.size()-1, tmp.fromStartEnd(tmp.start(),requiredAttendees.get(i).end(),true));
              }
          } else {
              busyTimeSlots.add(requiredAttendees.get(i));
          }
      }
      return busyTimeSlots;
      //TODO: Based on the busyTimeSlots of attendees, determine pockets of available time
      //whereby I can schedule the requested meeting
  }
}
