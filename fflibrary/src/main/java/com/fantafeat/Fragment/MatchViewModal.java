package com.fantafeat.Fragment;

import androidx.lifecycle.ViewModel;

import com.fantafeat.Model.MatchModel;

import java.util.List;

public class MatchViewModal extends ViewModel {
    List<MatchModel> matchModelList;

    public List<MatchModel> getMatchModelList() {
        return matchModelList;
    }

    public void setMatchModelList(List<MatchModel> matchModelList) {
        this.matchModelList = matchModelList;
    }
}
