package dispesas.com.utils;

import dispesas.com.Repository.DespesaRepository;
import dispesas.com.model.Despesa;
import dispesas.com.security.utilSecurity.GetUserById;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ToExcel {
    private final DespesaRepository despesaRepository;
    private final GetUserById getUserById;

    public ByteArrayInputStream exportarDespesas() {

        Long userId = getUserById.getUserById().getId();

        List<Despesa> despesas = despesaRepository.findByUserId(userId);

        return gerarExcel(despesas);
    }

    private  ByteArrayInputStream gerarExcel(List<Despesa> despesas) {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Despesas Totais");

            //Cabeçalho
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Id");
            header.createCell(1).setCellValue("Descricao");
            header.createCell(2).setCellValue("Valor");
            header.createCell(3).setCellValue("Tipo");
            header.createCell(4).setCellValue("Categoria");
            header.createCell(5).setCellValue("Metodo Pagamento");
            header.createCell(6).setCellValue("Status");
            header.createCell(7).setCellValue("Data Vencimento");
            header.createCell(8).setCellValue("Parcelas");
            header.createCell(9).setCellValue("Numero Parcelas");
            header.createCell(10).setCellValue("Recorrencia");
            header.createCell(11).setCellValue("Data criacao");
            header.createCell(12).setCellValue("Data atualizacao");

            int linha = 1;

            for (Despesa despesa : despesas) {

                Row row = sheet.createRow(linha++);

                row.createCell(0).setCellValue(despesa.getId());
                row.createCell(1).setCellValue(despesa.getDescription());
                row.createCell(2).setCellValue(despesa.getValue().doubleValue());
                row.createCell(3).setCellValue(despesa.getType().name());
                row.createCell(4).setCellValue(despesa.getCategory().name());
                row.createCell(5).setCellValue(despesa.getPaymentMethod().name());
                row.createCell(6).setCellValue(despesa.getStatus().name());
                row.createCell(7).setCellValue(despesa.getExpenseDate().toString());

                if (despesa.getInstallments() != null) {
                    row.createCell(8).setCellValue(despesa.getInstallments());
                }

                if (despesa.getInstallmentNumber() != null) {
                    row.createCell(9).setCellValue(despesa.getInstallmentNumber());
                }

                row.createCell(10).setCellValue(despesa.isRecurrent());
                row.createCell(11).setCellValue(despesa.getCreatedAt().toString());
                row.createCell(12).setCellValue(despesa.getUpdatedAt().toString());

            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);
            sheet.autoSizeColumn(9);
            sheet.autoSizeColumn(10);
            sheet.autoSizeColumn(11);
            sheet.autoSizeColumn(12);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

