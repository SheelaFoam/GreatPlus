package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by priyanka.sharma on 11/29/2016.
 */

public class HomeSecondBE {
    ArrayList<CompanyPerformanceBE> company_performance;
    ArrayList<CompanyPerformanceHeadingBE> company_performance_heading;
    ArrayList<PollsBE> polls;
    ArrayList<HomeSankalpBE> sankalp_story;
    ArrayList<HomeEventBE> event_list;
    ArrayList<Performance_homeBE> my_performance;
    ArrayList<UpcomingHolidayBE> upcoming_holiday;
    ArrayList<RquestInnerBE> request_center_inner_menu;
    ArrayList<ProfileImageBE> profile_image;

    public ArrayList<ProfileImageBE> getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(ArrayList<ProfileImageBE> profile_image) {
        this.profile_image = profile_image;
    }

    public ArrayList<CompanyPerformanceHeadingBE> getCompany_performance_heading() {
        return company_performance_heading;
    }

    public void setCompany_performance_heading(ArrayList<CompanyPerformanceHeadingBE> company_performance_heading) {
        this.company_performance_heading = company_performance_heading;
    }

    public ArrayList<RquestInnerBE> getRequest_center_inner_menu() {
        return request_center_inner_menu;
    }

    public void setRequest_center_inner_menu(ArrayList<RquestInnerBE> request_center_inner_menu) {
        this.request_center_inner_menu = request_center_inner_menu;
    }

    public ArrayList<UpcomingHolidayBE> getUpcoming_holiday() {
        return upcoming_holiday;
    }

    public void setUpcoming_holiday(ArrayList<UpcomingHolidayBE> upcoming_holiday) {
        this.upcoming_holiday = upcoming_holiday;
    }

    public ArrayList<Performance_homeBE> getMy_performance() {
        return my_performance;
    }

    public void setMy_performance(ArrayList<Performance_homeBE> my_performance) {
        this.my_performance = my_performance;
    }

    public ArrayList<HomeEventBE> getEvent_list() {
        return event_list;
    }

    public void setEvent_list(ArrayList<HomeEventBE> event_list) {
        this.event_list = event_list;
    }

    public ArrayList<HomeSankalpBE> getSankalp_story() {
        return sankalp_story;
    }

    public void setSankalp_story(ArrayList<HomeSankalpBE> sankalp_story) {
        this.sankalp_story = sankalp_story;
    }

    public ArrayList<PollsBE> getPolls() {
        return polls;
    }

    public void setPolls(ArrayList<PollsBE> polls) {
        this.polls = polls;
    }

    public ArrayList<CompanyPerformanceBE> getCompany_performance() {
        return company_performance;
    }

    public void setCompany_performance(ArrayList<CompanyPerformanceBE> company_performance) {
        this.company_performance = company_performance;
    }
}
