package com.cms.helpdesk.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {

    // Proccess
    SUCCESSFULLY_DEFAULT(200, "Proses Berhasil"),
    NOT_FOUND_DEFAULT(404, "Data tidak ditemukan"),
    FAILED_DEFAULT(400, "Proses Gagal"),
    SUCCESS_FAILED_DEFAULT(400, "Terdapat beberapa kesalahan request"),

    // Auth
    SUCCESSFULLY_LOGIN(200, "Login Berhasil"),
    SUCCESSFULLY_REGISTER(200, "Register Berhasil"),
    SUCCESSFULLY_LOGOUT(200, "Logout Berhasil"),
    FAILED_LOGOUT(404, "Logout Gagal"),
    FAILED_REGISTER(404, "Register Gagal"),
    FAILED_LOGIN(404, "Login Gagal"),

    // Exception
    EXCEPTION_NOT_FOUND(404, "Tidak ditemukan"),
    EXCEPTION_ALREADY_EXIST(302, "Data sudah ada"),
    EXCEPTION_BAD_REQUEST(400, "Request tidak sesuai"),
    EXCEPTION_NOT_SUPPORT_REQUEST(400, "Request tidak bisa dibaca"),
    EXCEPTION_INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int statusCode;
    private final String message;
}
