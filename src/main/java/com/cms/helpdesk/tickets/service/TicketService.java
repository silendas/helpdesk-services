package com.cms.helpdesk.tickets.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cms.helpdesk.attachments.dto.AttachmentRes;
import com.cms.helpdesk.attachments.filter.AttachmentFilter;
import com.cms.helpdesk.attachments.model.Attachment;
import com.cms.helpdesk.attachments.repository.AttachmentRepository;
import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.path.AttachmentPath;
import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.ConvertDate;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.common.reuse.PageConvert;
import com.cms.helpdesk.common.utils.FileUtil;
import com.cms.helpdesk.common.utils.ReportExcelUtil;
import com.cms.helpdesk.enums.PriorityEnum;
import com.cms.helpdesk.enums.tickets.StatusEnum;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.branch.repository.BranchRepository;
import com.cms.helpdesk.management.constraintcategory.model.ConstraintCategory;
import com.cms.helpdesk.management.constraintcategory.repository.ConstraintCategoryRepository;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.departments.repository.DepartmentRepository;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.management.regions.repository.RegionRepository;
import com.cms.helpdesk.management.targetcompletion.model.TargetCompletion;
import com.cms.helpdesk.management.targetcompletion.repository.TargetCompletionRepository;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.model.User;
import com.cms.helpdesk.management.users.repository.EmployeeRepository;
import com.cms.helpdesk.management.users.service.EmployeeService;
import com.cms.helpdesk.tickets.dto.CloseTicketDTO;
import com.cms.helpdesk.tickets.dto.CreateTicketDTO;
import com.cms.helpdesk.tickets.dto.OfficeRes;
import com.cms.helpdesk.tickets.dto.ProcessTicketDTO;
import com.cms.helpdesk.tickets.dto.TicketDetailRes;
import com.cms.helpdesk.tickets.dto.TicketDispositionDTO;
import com.cms.helpdesk.tickets.dto.TicketDispositionRes;
import com.cms.helpdesk.tickets.dto.TicketListRes;
import com.cms.helpdesk.tickets.filter.TicketFilter;
import com.cms.helpdesk.tickets.model.Ticket;
import com.cms.helpdesk.tickets.model.TicketDisposition;
import com.cms.helpdesk.tickets.repository.PaginateTicket;
import com.cms.helpdesk.tickets.repository.TicketDispositionRepository;
import com.cms.helpdesk.tickets.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PaginateTicket paginate;

    @Autowired
    private TicketDispositionRepository dispositionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ConstraintCategoryRepository constraintRepository;

    @Autowired
    private TargetCompletionRepository targetCompletionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReportExcelUtil reportUtil;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    private final String STORAGE_PATH_ATTACHMENT = AttachmentPath.STORAGE_PATH_ATTACHMENT;

    public ResponseEntity<Object> getTickets(int page, int size) {
        User getUserData = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userRole = getUserData.getRole().getName();
        Specification<Ticket> spec = Specification
                .where(new Filter<Ticket>().isNotDeleted())
                .and(new Filter<Ticket>().orderByIdDesc());

        if ("USER".equalsIgnoreCase(userRole)) {
            spec = spec.and(new TicketFilter().findByCreatedBy(getUserData.getUsername()));
        } else if ("SUPERVISOR".equalsIgnoreCase(userRole)) {
            spec = spec.and(
                    new TicketFilter().findTicketSupervisor(
                            Arrays.asList(PriorityEnum.LOW, PriorityEnum.MEDIUM),
                            getUserData.getEmployee().getRegion(),
                            getUserData.getEmployee().getBranch(),
                            getUserData.getUsername()));
        } else if ("HELPDESK".equalsIgnoreCase(userRole)) {
            spec = spec.and(
                    new TicketFilter().findTicketHelpdesk(
                            PriorityEnum.HIGH,
                            getUserData.getEmployee().getDepartment(),
                            getUserData.getUsername()));
        }

        Page<Ticket> res = paginate.findAll(spec, PageRequest.of(page, size));

        return Response.buildResponse(
                new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                        Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res),
                        buildResListTicket(res.getContent()),
                        null),
                1);
    }

    private List<TicketListRes> buildResListTicket(List<Ticket> ticket) {
        List<TicketListRes> res = new ArrayList<>();
        for (Ticket t : ticket) {
            res.add(buildResListTicket(t));
        }
        return res;
    }

    public TicketListRes buildResListTicket(Ticket ticket) {
        TicketListRes resList = new TicketListRes();
        resList.setId(ticket.getId());
        resList.setTicketNumber(ticket.getTicketNumber());
        resList.setCreatedAt(ticket.getCreatedAt());
        resList.setCreatedBy(ticket.getCreatedBy());
        resList.setStatus(ticket.getStatus());
        resList.setPriority(
                ticket.getConstraintCategoryId() != null ? ticket.getConstraintCategoryId().getPriority().toString()
                        : "");
        resList.setExternal(ticket.isExternal());

        OfficeRes officeRes = new OfficeRes();
        officeRes.setDepartment(ticket.getDepartmentId());
        officeRes.setRegion(ticket.getRegionId());
        officeRes.setBranch(ticket.getBranchId());
        resList.setOffice(officeRes);

        return resList;
    }

    public ResponseEntity<Object> findTicket(Long id) {

        Ticket ticket = getTicket(id);

        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, buildResTicketDetail(ticket), null), 1);

    }

    public TicketDetailRes buildResTicketDetail(Ticket ticket) {
        TicketDetailRes ticketRes = new TicketDetailRes();
        ticketRes.setId(ticket.getId());
        ticketRes.setTicketNumber(ticket.getTicketNumber());
        ticketRes.setRegion(ticket.getRegionId());
        ticketRes.setBranch(ticket.getBranchId());
        ticketRes.setDepartment(ticket.getDepartmentId());
        ticketRes.setConstraintCategory(ticket.getConstraintCategoryId());
        ticketRes.setPriority(ticket.getConstraintCategoryId().getPriority().toString());
        ticketRes.setDescription(ticket.getDescription());
        ticketRes.setStatus(ticket.getStatus());
        ticketRes.setTargetCompletion(ticket.getTargetCompletion());
        ticketRes.setTimeCompletion(ticket.getTimeCompletion());
        ticketRes.setDescriptionCompletion(ticket.getDescriptionCompletion());
        ticketRes.setProcessBy(ticket.getProcessBy() != null ? ticket.getProcessBy().getName() : null);
        ticketRes.setProcessAt(ticket.getProcessAt());
        ticketRes.setRequesterNip(ticket.getRequesterNip());
        ticketRes.setRequesterEmail(ticket.getRequesterEmail());
        ticketRes.setExternal(ticket.isExternal());
        ticketRes.setCreatedBy(ticket.getCreatedBy());
        ticketRes.setCreatedAt(ticket.getCreatedAt());

        Specification<TicketDisposition> spec = Specification.where(new TicketFilter().findByTicketId(ticket.getId()));
        List<TicketDisposition> ticketDisposition = dispositionRepository.findAll(spec);
        List<TicketDispositionRes> dispositionResponses = new ArrayList<>();
        for (TicketDisposition disposition : ticketDisposition) {
            TicketDispositionRes dispositionRes = buildTicketDispositionRes(disposition);
            dispositionResponses.add(dispositionRes);
        }
        ticketRes.setDisposition(dispositionResponses);

        Specification<Attachment> specAttachment = Specification
                .where(new AttachmentFilter().findByTicketId(ticket.getId()));
        List<Attachment> ticketAttachment = attachmentRepository.findAll(specAttachment);
        List<AttachmentRes> attachmentResponses = new ArrayList<>();
        for (Attachment attachment : ticketAttachment) {
            AttachmentRes attachmentRes = buildAttachmentRes(attachment);
            attachmentResponses.add(attachmentRes);
        }
        ticketRes.setAttachment(attachmentResponses);

        return ticketRes;
    }

    public TicketDispositionRes buildTicketDispositionRes(TicketDisposition ticketDisposition) {
        TicketDispositionRes ticketDispositionRes = new TicketDispositionRes();
        ticketDispositionRes.setUserFrom(ticketDisposition.getUserFrom().getName());
        ticketDispositionRes.setUserTo(ticketDisposition.getUserTo().getName());
        ticketDispositionRes.setDescription(ticketDisposition.getDescription());
        return ticketDispositionRes;
    }

    public AttachmentRes buildAttachmentRes(Attachment attachment) {
        AttachmentRes attachmentRes = new AttachmentRes();
        attachmentRes.setId(attachment.getId());
        attachmentRes.setFilename(attachment.getFilename());
        attachmentRes.setFiletype(attachment.getFileType());
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(BasePath.BASE_ATTACHMENTS)
                .path("/view/")
                .path(attachment.getFilename())
                .toUriString();
        attachmentRes.setFileurl(fileUrl);
        return attachmentRes;
    }

    public ResponseEntity<Object> createTicket(CreateTicketDTO dto) throws IOException {

        User getUserData = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String userRole = getUserData.getRole().getName();
        Department userDepartment = getUserData.getEmployee().getDepartment();
        Region userRegion = getUserData.getEmployee().getRegion();
        Branch userBranch = getUserData.getEmployee().getBranch();

        // Get the current date in YYYYMMDD format
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDateStr = today.format(formatter);

        String datePattern = "#" + currentDateStr + "-%";

        // Retrieve the last ticket number created today, limited to the most recent one
        List<String> lastTicketNumbers = ticketRepository.findLastTicketNumberByDate(datePattern);

        String lastTicketNumber = lastTicketNumbers.isEmpty() ? null : lastTicketNumbers.get(0);

        int nextNum = 1; // Default to 1 if no tickets have been created today
        if (lastTicketNumber != null) {
            String[] parts = lastTicketNumber.split("-");
            if (parts.length == 2 && parts[0].equals("#" + currentDateStr)) {
                try {
                    nextNum = Integer.parseInt(parts[1]) + 1; // Increment the last number found
                } catch (NumberFormatException e) {
                    // Handle the exception if the number part is not a valid integer
                    nextNum = 1; // Fallback to 1 in case of an error
                }
            }
        }

        // Generate the new ticket number
        String newTicketNumber = "#" + currentDateStr + "-" + nextNum;

        Department departmentId;
        Region regionId;
        Branch branchId;

        if (userRole.equalsIgnoreCase("USER")) {
            departmentId = userDepartment;
            regionId = userRegion;
            branchId = userBranch;
        } else {
            departmentId = getDepartment(dto.getDepartmentId());
            regionId = getRegion(dto.getRegionId());
            branchId = getBranch(dto.getBranchId());
        }

        Ticket ticket = new Ticket();
        ticket.setTicketNumber(newTicketNumber);
        ticket.setDescription(dto.getDescription());
        ticket.setConstraintCategoryId(getConstraint(dto.getConstraintCategoryId()));
        ticket.setDepartmentId(departmentId);
        ticket.setRegionId(regionId);
        ticket.setBranchId(branchId);
        ticket.setStatus(StatusEnum.OPEN);
        ticket.setExternal(false);

        Ticket savedTicket = ticketRepository.save(ticket);

        if (dto.getAttachments() != null && !dto.getAttachments().isEmpty()) {
            List<Attachment> attachments = new ArrayList<>();
            int fileCounter = 1;
            for (MultipartFile file : dto.getAttachments()) {
                String filetypeStr = file.getContentType();

                String path = STORAGE_PATH_ATTACHMENT;

                String fileName = fileUtil.store(file, path,
                        fileCounter + "_" + savedTicket.getTicketNumber() + "_"
                                + StringUtils.cleanPath(file.getOriginalFilename()));

                Attachment attachment = new Attachment();
                attachment.setTicket(savedTicket);
                attachment.setFilename(fileName);
                attachment.setFileType(filetypeStr);
                attachments.add(attachment);

                fileCounter++;
            }

            attachmentRepository.saveAll(attachments); // Save all attachments at once
        }

        return Response.buildResponse(new GlobalDto(
                Message.SUCCESSFULLY_DEFAULT.getStatusCode(),
                null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(),
                null,
                savedTicket,
                null),
                0);
    }

    public ResponseEntity<Object> processTicket(Long id, ProcessTicketDTO dto) {
        User getUserData = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userRole = getUserData.getRole().getName();
        Timestamp currentTime = Timestamp.from(Instant.now());

        if (!userRole.equalsIgnoreCase("USER")) {
            Ticket ticket = getTicket(id);

            ConstraintCategory constraintCategory = ticket.getConstraintCategoryId();
            TargetCompletion targetCompletion = constraintCategory.getTargetCompletionId();

            Instant targetCompletionTime = currentTime.toInstant();
            switch (targetCompletion.getTimeInterval()) {
                case MINUTES:
                    targetCompletionTime = targetCompletionTime.plus(targetCompletion.getValue().longValue(),
                            ChronoUnit.MINUTES);
                    break;
                case HOURS:
                    targetCompletionTime = targetCompletionTime.plus(targetCompletion.getValue().longValue(),
                            ChronoUnit.HOURS);
                    break;
                case DAYS:
                    targetCompletionTime = targetCompletionTime.plus(targetCompletion.getValue().longValue(),
                            ChronoUnit.DAYS);
                    break;
            }

            ticket.setProcessBy(getUserData.getEmployee());
            ticket.setProcessAt(currentTime);
            ticket.setTargetCompletion(Timestamp.from(targetCompletionTime));
            ticket.setStatus(StatusEnum.PROGRESS);
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), null, ticketRepository.save(ticket), null), 0);
        } else {
            return Response.buildResponse(new GlobalDto(Message.UNAUTORIZED_TICKET.getStatusCode(), null,
                    Message.UNAUTORIZED_TICKET.getMessage(), null, null, null), 0);
        }
    }

    public ResponseEntity<Object> closedTicket(Long id, CloseTicketDTO dto) {
        User getUserData = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userNip = getUserData.getEmployee().getNip();
        Timestamp currentTime = Timestamp.from(Instant.now());

        Ticket ticket = getTicket(id);
        String processBy = ticket.getProcessBy().getNip();

        if (processBy.equals(userNip)) {
            ticket.setTimeCompletion(currentTime);
            ticket.setDescriptionCompletion(dto.getDescriptionCompletion());
            ticket.setStatus(StatusEnum.CLOSED);
        } else {
            return Response.buildResponse(new GlobalDto(Message.UNAUTORIZED_TICKET.getStatusCode(), null,
                    Message.UNAUTORIZED_TICKET.getMessage(), null, null, null), 0);
        }

        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, ticketRepository.save(ticket), null), 0);
    }

    public ResponseEntity<Object> dispositionTicket(Long id, TicketDispositionDTO dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Ticket ticket = getTicket(id);

        TicketDisposition disposition = new TicketDisposition();

        disposition.setTicket(ticket);
        disposition.setUserFrom(user.getEmployee());
        disposition.setUserTo(employeeService.getEmployeeByNip(dto.getUserTo()));
        disposition.setDescription(dto.getDescription());

        dispositionStatus(ticket);

        return Response.buildResponse(
                new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                        Message.SUCCESSFULLY_DEFAULT.getMessage(), null, dispositionRepository.save(disposition), null),
                0);
    }

    public void dispositionStatus(Ticket ticket) {
        ticket.setStatus(StatusEnum.DISPOSITION);
        ticketRepository.save(ticket);
    }

    public ResponseEntity<InputStreamResource> downloadReport(Date start, Date end) {
        Specification<Ticket> spec = Specification
                .where(new Filter<Ticket>().createdAtBetween(start, end))
                .and(new Filter<Ticket>().isNotDeleted())
                .and(new Filter<Ticket>().orderByIdAsc());
        List<Ticket> tickets = ticketRepository.findAll(spec);

        List<String> headers = Arrays.asList("Nomor Tiket", "Urgensi", "Dibuat Oleh", "Diproses Oleh", "Tanggal Terbuat", "Tanggal Diproses", "Target Penyelesaian", "Tanggal Penyelesaian", "Waktu Penyelesaian", "Status Penyelesaian", "Departemen", "Region", "Branch", "Kategori Kebutuhan", "Status", "Deskripsi", "Deskripsi Penyelesaian");

        // Dibuat Oleh, Urgensi -> CC.Priority, Deskripsi Penyelesaian, Status Penyelesaian
        List<Map<String, Object>> data = tickets.stream().map(ticket -> {
            Map<String, Object> map = new HashMap<>();
            map.put("Nomor Tiket", ticket.getTicketNumber());
            map.put("Urgensi", ticket.getConstraintCategoryId() != null ? ticket.getConstraintCategoryId().getPriority().toString() : "");
            map.put("Dibuat Oleh", ticket.getCreatedBy() != null ? employeeService.getEmployee(ticket.getCreatedBy()).getName() : "");
            map.put("Diproses Oleh", ticket.getProcessBy() != null ? ticket.getProcessBy().getName() : "");
            map.put("Tanggal Terbuat", ConvertDate.formatToYMDT(ticket.getCreatedAt()));
            map.put("Tanggal Diproses", ConvertDate.formatToYMDT(ticket.getProcessAt()));
            map.put("Target Penyelesaian", ConvertDate.formatToYMDT(ticket.getTargetCompletion()));
            map.put("Tanggal Penyelesaian", ConvertDate.formatToYMDT(ticket.getTimeCompletion()));
            map.put("Waktu Penyelesaian", makeTimeCompletion(ticket.getTimeCompletion(), ticket.getTargetCompletion()));
            map.put("Status Penyelesaian", makeStatusCompletion(ticket.getTimeCompletion(), ticket.getTargetCompletion()));
            map.put("Departemen", ticket.getDepartmentId() != null ? ticket.getDepartmentId().getName() : "");
            map.put("Region", ticket.getRegionId() != null ? ticket.getRegionId().getName() : "");
            map.put("Branch", ticket.getBranchId() != null ? ticket.getBranchId().getName() : "");
            map.put("Kategori Kebutuhan", ticket.getConstraintCategoryId() != null ? ticket.getConstraintCategoryId().getName() : "");
            map.put("Status", ticket.getStatus().toString());
            map.put("Deskripsi", ticket.getDescription());
            map.put("Deskripsi Penyelesaian", ticket.getDescriptionCompletion());
            return map;
        }).collect(Collectors.toList());

        return reportUtil.generate("Report Tickets", headers, data);
    }

    public static String makeStatusCompletion(Date firstDate, Date secondDate) {
        if(firstDate == null || secondDate == null) return null;
        long diffInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());

        StringBuilder result = new StringBuilder();

        if (diffInMillies > 0) {
            result.append("Over Time");
        } else {
            result.append("Under Time");
        }
        return result.toString().trim();
    }

    public static String makeTimeCompletion(Date firstDate, Date secondDate) {
        if(firstDate == null || secondDate == null) return null;
        long diffInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());

        long hours = TimeUnit.MILLISECONDS.toHours(diffInMillies);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillies) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillies) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(hours).append(" Jam ");
        }
        if (minutes > 0) {
            result.append(minutes).append(" Menit ");
        }
        if (seconds > 0) {
            result.append(seconds).append(" Detik");
        }

        return result.toString().trim();
    }

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket Not Found"));
    }

    public Department getDepartment(Long id) {
        if (id == null) {
            return null;
        }
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department Not Found"));
    }

    public Region getRegion(Long id) {
        if (id == null) {
            return null;
        }
        return regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region Not Found"));
    }

    public Branch getBranch(Long id) {
        if (id == null) {
            return null;
        }
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch Not Found"));
    }

    public ConstraintCategory getConstraint(Long id) {
        return constraintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Constraint Not Found"));
    }

    public TargetCompletion getTargetCompletion(Long id) {
        return targetCompletionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Target Completion Not Found"));
    }

    public Employee getEmployee(String nip) {
        return employeeRepository.findById(nip)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id : " + nip + " not found"));
    }
}
