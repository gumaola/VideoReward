package cn.nano.common.regionselector;

import android.content.Context;
import android.os.HandlerThread;

import androidx.annotation.WorkerThread;

import java.util.HashMap;
import java.util.List;

import cn.nano.common.regionselector.bean.AreaListBean;
import cn.nano.common.regionselector.bean.CityListBean;
import cn.nano.common.regionselector.bean.ProvinceListBean;
import cn.nano.common.regionselector.bean.RegionList;
import cn.nano.common.thread.WorkThread;
import cn.nano.common.utils.FileUtil;
import cn.nano.common.utils.GsonUtil;

public class RegionDataModel {

    private static RegionDataModel instance;


    private WorkThread loadDataThread;

    //加载出的数据
    private RegionList regions;
    private HashMap<String, ProvinceListBean> provinceMap;
    private HashMap<String, CityListBean> cityMap;
    private HashMap<String, AreaListBean> areaMap;


    private RegionDataModel() {
        provinceMap = new HashMap<>();
        cityMap = new HashMap<>();
        areaMap = new HashMap<>();


        loadDataThread = new WorkThread("loadData");
        loadDataThread.start();
    }

    public static RegionDataModel getModel() {
        if (instance == null) {
            synchronized (RegionDataModel.class) {
                instance = new RegionDataModel();
            }
        }

        return instance;
    }

    public interface RegionDataLoadListener {
        void onLoadSuccess();

        void onLoadFail();
    }

    public void installData(final Context context, final RegionDataLoadListener loadListener) {
        loadDataThread.post(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = FileUtil.readAsset2String(context, "region.json");
                    regions = GsonUtil.createGson().fromJson(json, RegionList.class);

                    //read data
                    if (regions == null || regions.getProvinceList() == null) {
                        if (loadListener != null) {
                            loadListener.onLoadFail();
                        }
                        return;
                    }

                    List<ProvinceListBean> provinces = regions.getProvinceList();
                    String provinceCode;

                    List<CityListBean> cities;
                    String cityCode;

                    List<AreaListBean> areas;
                    String areaCode;

                    for (ProvinceListBean province : provinces) {
                        provinceCode = province.getCode();
                        provinceMap.put(provinceCode, province);

                        //child
                        cities = province.getCityList();
                        if (cities == null || cities.size() <= 0) {
                            continue;
                        }

                        for (CityListBean city : cities) {
                            cityCode = city.getCode();
                            cityMap.put(cityCode, city);

                            //直辖市或者特别行政区
                            if (cityCode.equalsIgnoreCase(provinceCode)) {
                                province.setDirectCity(true);
                            }

                            //child
                            areas = city.getAreaList();
                            if (areas == null || areas.size() <= 0) {
                                continue;
                            }

                            for (AreaListBean area : areas) {
                                areaCode = area.getCode();
                                areaMap.put(areaCode, area);

                                //筛选特出地区
                                if (areaCode.equalsIgnoreCase(cityCode)) {
                                    city.setHasChildArea(false);
                                }

                                if (areaCode.equalsIgnoreCase(provinceCode)) {
                                    province.setHasChildArea(false);
                                }

                                area.setHasChildArea(false);
                            }
                        }
                    }

                    //加载完成
                    if (loadListener != null) {
                        loadListener.onLoadSuccess();
                    }


                } catch (Exception e) {
                    cityMap.clear();
                    areaMap.clear();
                    if (loadListener != null) {
                        loadListener.onLoadFail();
                    }
                }
            }
        });
    }

    public List<ProvinceListBean> getRegions() {
        return regions.getProvinceList();
    }

    public ProvinceListBean getProvice(String code) {
        return provinceMap.get(code);
    }

    public CityListBean getCity(String code) {
        return cityMap.get(code);
    }

    public AreaListBean getArea(String code) {
        return areaMap.get(code);
    }

    //清理单例内存
    public void release() {
        cityMap.clear();
        areaMap.clear();
        loadDataThread.release();
        loadDataThread = null;
        instance = null;
    }
}
