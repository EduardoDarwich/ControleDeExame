package com.SCX.ControleDeExame.dataTransferObject.examsDTO;


import java.util.UUID;

public record ExamsDTO(String cid, String status, String result_value, String result_file_url, String observation, UUID id) {


}
