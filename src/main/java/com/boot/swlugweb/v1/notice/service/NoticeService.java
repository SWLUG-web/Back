
package com.boot.swlugweb.v1.notice.service;

import com.boot.swlugweb.v1.notice.domain.entity.Notice;
import com.boot.swlugweb.v1.notice.domain.repository.NoticeRepository;
import com.boot.swlugweb.v1.notice.dto.CreateNoticeDto;
import com.boot.swlugweb.v1.notice.dto.UpdateNoticeDto;
import com.boot.swlugweb.v1.notice.dto.ViewNoticeDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    //사진 업로드
    @Value("${uploadPath}")
    private String uploadPath;

    //공지글 생성
    @Transactional
    public Long createNotice(CreateNoticeDto createNoticeDto, MultipartFile noticeImg) throws IOException {
        Notice notice = new Notice();
        notice.setUserId(String.valueOf(createNoticeDto.getId()));
        notice.setTitle(createNoticeDto.getTitle());
        notice.setContent(createNoticeDto.getContent());

        //사진
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString()+"_"+noticeImg.getOriginalFilename();
        File noticeImgFile = new File(uploadPath, fileName);
        noticeImg.transferTo(noticeImgFile);
        notice.setImage(fileName);
        notice.setImagePath(uploadPath+"/"+fileName);
        //이미지 로직

//        notice.setCreateAt(createNoticeDto.getCreateAt());
        notice.setCreateAt(createNoticeDto.getCreateAt() != null ? createNoticeDto.getCreateAt() : LocalDateTime.now());
        notice.setUpdateAt(LocalDateTime.now());
        noticeRepository.save(notice);
        return notice.getNoticeId();

    }

//    //공지글 수정
    @Transactional
    public void updateNotice(Long noticeId,UpdateNoticeDto updateNoticeDto, MultipartFile noticeImg) throws IOException {
        Optional<Notice> optionalNotice = Optional.ofNullable(noticeRepository.findById(noticeId))
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if(optionalNotice.isPresent()) {
            Notice notice = optionalNotice.get();
            //업데이트할 필드만 업데이트

            if(updateNoticeDto.getTitle()!=null) {
                notice.setTitle(updateNoticeDto.getTitle());
            }
            if(updateNoticeDto.getContent()!=null) {
                notice.setContent(updateNoticeDto.getContent());
            }
            if(noticeImg!=null) {
                UUID uuid = UUID.randomUUID();
                String fileName = uuid.toString()+"_"+noticeImg.getOriginalFilename();
                File boardImgFile = new File(uploadPath, fileName);
                noticeImg.transferTo(boardImgFile);
                notice.setImage(fileName);
                notice.setImagePath(uploadPath+"/"+fileName);
            }
            notice.setUpdateAt(updateNoticeDto.getUpdateAt());

            noticeRepository.save(notice);

        }





    }

//    @Transactional
//    public void updateNotice(UpdateNoticeDto updateNoticeDto){
//        Notice notice = noticeRepository.findById(updateNoticeDto.getNoticeId().longValue())  // 수정된 부분
//                .orElseThrow(() -> new IllegalArgumentException("공지글을 찾을 수 없습니다."));
//
//        notice.setUserId(String.valueOf(updateNoticeDto.getId()));
//        notice.setTitle(updateNoticeDto.getTitle());
//        notice.setContent(updateNoticeDto.getContent());
//        notice.setImagePath(updateNoticeDto.getImagePath());
//        notice.setUpdateAt(updateNoticeDto.getUpdateAt());
//        noticeRepository.save(notice);
//    }


    //공지글 삭제
    @Transactional
    public void deleteNotice(Long noticeId){
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(()-> new IllegalArgumentException("공지글을 찾을 수 없습니다."));

        noticeRepository.delete(notice);
        }


    //공지글 조회
    @Transactional
    public ViewNoticeDto getNotice(Long noticeId){
        Notice notice=noticeRepository.findById(noticeId)
                .orElseThrow(()-> new IllegalArgumentException("공지글을 찾을 수 없습니다."));
        return ViewNoticeDto.builder()
                .noticeId((int) notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .imagePath(notice.getImagePath())
                .updateAt(notice.getUpdateAt())
                .createAt(notice.getCreateAt())
                .build();

    }

    //공지글 리스트
    @Transactional
    public List<ViewNoticeDto> getNoticeList() {
        List<Notice> noticeList = noticeRepository.findAll();  // 모든 공지글 조회

        return noticeList.stream()
                .map(notice -> ViewNoticeDto.builder()
                        .noticeId((int) notice.getNoticeId())
                        .title(notice.getTitle())
                        .content(notice.getContent())
                        .imagePath(notice.getImagePath())
                        .updateAt(notice.getUpdateAt())
                        .createAt(notice.getCreateAt())
                        .build())
                .toList();

    }

    // 공지글 검색 기능
    @Transactional
    public List<ViewNoticeDto> searchNotices(String keyword) {
        List<Notice> noticeList = noticeRepository.findAll();
        return noticeList.stream()
                .filter(notice -> notice.getTitle().contains(keyword) || notice.getContent().contains(keyword))
                .map(notice -> ViewNoticeDto.builder()
                        .noticeId((int) notice.getNoticeId())
                        .title(notice.getTitle())
                        .content(notice.getContent())
                        .imagePath(notice.getImagePath())
                        .updateAt(notice.getUpdateAt())
                        .createAt(notice.getCreateAt())
                        .build())
                .collect(Collectors.toList());
    }




}

