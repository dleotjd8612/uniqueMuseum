package ac.kr.hanbat.uniquemuseum.service;

import ac.kr.hanbat.uniquemuseum.dto.MuseumDTO;
import ac.kr.hanbat.uniquemuseum.dto.MuseumImageDTO;
import ac.kr.hanbat.uniquemuseum.dto.PageRequestDTO;
import ac.kr.hanbat.uniquemuseum.dto.PageResultDTO;
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
    PageResultDTO<MuseumDTO, Object[]> getList(PageRequestDTO pageRequestDTO); // 박물관 리스트
    MuseumDTO getMuseum(Long mno); // 박물관 상세 정보

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

    default MuseumDTO entitiesToDTO(Museum museum, List<MuseumImage> museumImages, Double avg, Long reviewCnt) {
        MuseumDTO museumDTO = MuseumDTO.builder()
                .mno(museum.getMno())
                .name(museum.getName())
                .address(museum.getAddress())
                .number(museum.getNumber())
                .convenienceFacilityInformation(museum.getConvenienceFacilityInformation())
                .weekdaysOpen(museum.getWeekdaysOpen().format(DateTimeFormatter.ofPattern("mm:ss")))
                .weekdaysClose(museum.getWeekdaysClose().format(DateTimeFormatter.ofPattern("mm:ss")))
                .weekendOpen(museum.getWeekendOpen().format(DateTimeFormatter.ofPattern("mm:ss")))
                .weekendClose(museum.getWeekendClose().format(DateTimeFormatter.ofPattern("mm:ss")))
                .closingInformation(museum.getClosingInformation())
                .adultAdmissionFee(museum.getAdultAdmissionFee())
                .teenagerAdmissionFee(museum.getTeenagerAdmissionFee())
                .childrenAdmissionFee(museum.getChildrenAdmissionFee())
                .admissionFeeInformation(museum.getAdmissionFeeInformation())
                .regDate(museum.getRegDate())
                .modDate(museum.getModDate())
                .build();

        List<MuseumImageDTO> museumImageDTOList = museumImages.stream().map(museumImage -> {
           return MuseumImageDTO.builder()
                .imgName(museumImage.getImgName())
                .path(museumImage.getPath())
                .uuid(museumImage.getUuid())
                .build();
        }).collect(Collectors.toList());

        museumDTO.setImageDTOList(museumImageDTOList);
        museumDTO.setAvg(avg);
        museumDTO.setReviewCnt(reviewCnt.intValue());

        return museumDTO;
    }
}
