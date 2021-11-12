package ac.kr.hanbat.uniquemuseum.service;

import ac.kr.hanbat.uniquemuseum.dto.MuseumDTO;
import ac.kr.hanbat.uniquemuseum.dto.MuseumImageDTO;
import ac.kr.hanbat.uniquemuseum.entity.Museum;
import ac.kr.hanbat.uniquemuseum.entity.MuseumImage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MuseumService {
    Long register(MuseumDTO museumDTO); // 박물관 등록

    default Map<String, Object> dtoToEntity(MuseumDTO museumDTO) {
        Map<String, Object> entityMap = new HashMap<>();

        Museum museum = Museum.builder()
                .mno(museumDTO.getMno())
                .name(museumDTO.getName())
                .address(museumDTO.getAddress())
                .number(museumDTO.getNumber())
                .convenienceFacilityInformation(museumDTO.getConvenienceFacilityInformation())
                .weekdaysOpen(LocalTime.parse(museumDTO.getWeekdaysOpen(), DateTimeFormatter.ISO_LOCAL_TIME))
                .weekdaysClose(LocalTime.parse(museumDTO.getWeekdaysClose(), DateTimeFormatter.ISO_LOCAL_TIME))
                .weekendOpen(LocalTime.parse(museumDTO.getWeekendOpen(), DateTimeFormatter.ISO_LOCAL_TIME))
                .weekendClose(LocalTime.parse(museumDTO.getWeekendClose(),DateTimeFormatter.ISO_LOCAL_TIME))
                .closingInformation(museumDTO.getClosingInformation())
                .adultAdmissionFee(museumDTO.getAdultAdmissionFee())
                .teenagerAdmissionFee(museumDTO.getTeenagerAdmissionFee())
                .childrenAdmissionFee(museumDTO.getChildrenAdmissionFee())
                .admissionFeeInformation(museumDTO.getAdmissionFeeInformation())
                .build();

        entityMap.put("museum", museum);

        List<MuseumImageDTO> imageDTOList = museumDTO.getImageDTOList();
        System.out.println("MuseumService - cccccccccccccccccc:" + imageDTOList.toString());

        if(imageDTOList != null && imageDTOList.size() > 0) {
            List<MuseumImage> museumImageList = imageDTOList.stream().map(museumImageDTO -> {
                MuseumImage museumImage = MuseumImage.builder()
                        .path(museumImageDTO.getPath())
                        .imgName(museumImageDTO.getImgName())
                        .uuid(museumImageDTO.getUuid())
                        .museum(museum)
                        .build();
                return museumImage;
            }).collect(Collectors.toList());
            entityMap.put("imgList", museumImageList);
        }
        return entityMap;
    }
}
