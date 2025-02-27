package org.leon.astonhome.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.leon.astonhome.dto.PurchaseDTO;
import org.leon.astonhome.mapper.PurchaseMapper;
import org.leon.astonhome.repository.PurchaseRepository;
import org.leon.astonhome.service.PurchaseService;
import org.leon.astonhome.util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/purchases/*")
public class PurchaseServlet extends HttpServlet {
    private final PurchaseService purchaseService = new PurchaseService(new PurchaseRepository(), new PurchaseMapper());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
            JsonUtil.sendJsonResponse(resp, purchases, HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                PurchaseDTO purchase = purchaseService.getPurchaseById(id);
                JsonUtil.checkExists(purchase, "Purchase not found", HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendJsonResponse(resp, purchase, HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
            } catch (JsonUtil.NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PurchaseDTO purchaseDto = JsonUtil.parseJsonRequest(req, PurchaseDTO.class);
        try {
            JsonUtil.checkDto(purchaseDto);
            PurchaseDTO createdPurchase = purchaseService.createPurchase(purchaseDto);
            JsonUtil.sendJsonResponse(resp, createdPurchase, HttpServletResponse.SC_CREATED);
        } catch (JsonUtil.InvalidDtoException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PurchaseDTO purchaseDto = JsonUtil.parseJsonRequest(req, PurchaseDTO.class);
        try {
            JsonUtil.checkDto(purchaseDto);
            boolean updated = purchaseService.updatePurchase(purchaseDto.getPurchaseId(), purchaseDto);
            if (updated) {
                JsonUtil.sendJsonResponse(resp, purchaseDto, HttpServletResponse.SC_OK);
            } else {
                throw new JsonUtil.NotFoundException("Purchase not found");
            }
        } catch (JsonUtil.InvalidDtoException | JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendErrorResponse(resp, "Missing purchase ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = purchaseService.deletePurchase(id);
            JsonUtil.checkDeletion(deleted, "Purchase not found", HttpServletResponse.SC_NOT_FOUND);
            JsonUtil.sendJsonResponse(resp, "Purchase deleted", HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (JsonUtil.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
