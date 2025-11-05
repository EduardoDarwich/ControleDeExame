package com.SCX.ControleDeExame.domain.filePath;

import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "file_path")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class FilePath {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "exams_request_id")
    private ExamsRequest examsRequest;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

}