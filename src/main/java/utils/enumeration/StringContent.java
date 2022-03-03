package utils.enumeration;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import ddd.system.sensitive_words.SensitiveWord;
import ddd.system.sensitive_words.port_adapter.ISensitiveWordRepository;
import ddd.users.user.User;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class StringContent {

    @Autowired
    private ISensitiveWordRepository sensitiveWordRepository;
    // content push notification
    //accept friend
    public static String AGREE_FRIEND = " đã chấp nhận lời mời kết bạn của bạn.";
    public static String NEW_MESSAGE = " gửi tới nhóm ";
    //add point user report
    public static String REPORT_RIGHT = "Bạn vừa được cộng điểm sau khi gửi đúng cảnh báo";
    public static String CONFIRM_RIGHT = "Bạn vừa được cộng điểm sau khi xác nhận đúng cảnh báo";

    public static String CONTENT_NOTIFY_MESSAGE = "Đã gửi {image} ảnh";//request
    public static String CONTENT_NOTIFY_RECORDER = "Đã gửi một tin nhắn thoại";//request
    public static String CONTENT_NOTIFY_EMOTICON = "Đã gửi một nhãn dán";//request
    public static String CONTENT_NOTIFY_ADD = "Bạn đã được thêm vào nhóm {name}";//request
    public static String CONTENT_NOTIFY_JOIN = "{full_name} đã yêu cầu tham gia nhóm {name}";//request
    public static String CONTENT_NOTIFY_TAG = "{userPost} vừa thêm bạn vào bài viết";//tag
    public static String CONFIRM_ACTIVE = "{userConfirm} đã xác nhận cảnh báo của bạn là Đúng";
    public static String CONFIRM_INACTIVE = "{userConfirm} đã xác nhận cảnh báo của bạn là Sai";
    public static String CONTENT_NOTIFY_REQUEST = " đã gửi cho bạn lời mời kết bạn";//request
    public static String ADD_POINT_FIRST_REPORT = "Cộng điểm khi phản hồi đúng đầu tiên ";
    public static String ADD_POINT_FIRST_REPORT_WEEK = "Cộng điểm khi phản hồi đúng đầu tiên trong tuần ";

    public static String CONFIRM_CHANGE_OIL = "Bạn đã đến hạn thay nhớt định kỳ. Bạn đã thay nhớt chưa";
    public static String CONFIRM_MAINTENANCE = "Bạn đã đến hạn bảo dưỡng xe định kỳ. Bạn đã đi bảo dưỡng xe chưa";
    public static String TITLE_CREATE_ORDER = "Cám ơn quý khách {fullName} đã đặt hàng tại Widdy!";
    public static String TITLE_SHIPMENT_ORDER = "Đơn hàng đã sẵn sàng giao đến quý khách !";
    public static String CONTENT_CREATE_ORDER = "Widdy rất vui thông báo đến quý khách đơn hàng #{code} của quý khách đã được tiếp nhận và đang trong quá trình xử lý.";
    public static String CONTENT_SHIPMENT_ORDER = "Widdy rất vui thông báo đơn hàng #{code} của quý khách đã sẵn sàng giao đến quý khách. Quý khách vui lòng bật chuông và lưu ý điện thoại để nhận cuộc gọi khi shipper đến giao hàng.";

    public static String SUBJECT_CREATE_ORDER = "Thông báo xác nhận đơn hàng";
    public static String SUBJECT_SHIPMENT_ORDER = "Cập nhật thông tin giao hàng cho đơn hàng #{code}";

    public static String DESCRIPTION_CREATE_ORDER = "Widdy sẽ thông báo tới quý khách ngay khi hàng chuẩn bị được giao.";
    public static String CONTENT_NOTIFY = "Gần bạn đang có quà tặng, cùng khám phá app Widdy để nhận quà ngay nhé";//voucher
    public static String CONTENT_NOTIFY_WARNING = "Bạn đã bị khóa chức năng gửi cảnh báo [name] vì đã gửi quá [x] cảnh báo trong [y] phút. Chức năng sẽ được mở lại sau [z] phút, Xin cảm ơn.";
    public static String TITLE_NOTIFY_WARNING = "Thông báo khóa tính năng";
    public static String CONTENT_OTP = "Mã xác thực WIDDY của bạn là {code}";
    public static String WILL_TALE_PLACE_TOMORROW = " sẽ diễn ra vào ngày mai";
    public static String WILL_TALE_PLACE_ON = " sẽ diễn ra vào ngày ";
    public static String TITLE_NOTIFY_REMINDER = "Nhắc nhở";
    public static String TITLE_ACCEPT = "Duyệt địa điểm";
    public static String TITLE_CORONA = "[Quan trọng] Có điểm cách ly mới gần bạn!";
    public static String CONTENT_COMPLETE_PATH = "Bạn nhận được {point} điểm khi hoàn thành nhiệm vụ di chuyển.";
    public static String TITLE_COMPLETE_MISSION = "Hoàn thành nhiệm vụ";
    public static List<String> listProvinceHaveGroupDistrict = Arrays.asList("HO CHI MINH");
    public static List<String> listTypeMessNoDisplay = Arrays.asList("join","district","province", "leave");
    public static String DISTRICT_DEFAULT = "Thủ Đức";
    public static String NATION_DEFAULT = "Việt Nam";
    public static String PROVINCE_DEFAULT = "Hồ Chí Minh";
    public static String NO_UPDATE = "Chưa cập nhật";
    // group
    public static String DISBAND_GROUP_NOTIFICATION = "Nhóm {groupName} đã giải tán bởi {adminName}";
    public static String DISBAND_GROUP_TITLE = "Nhóm {groupName} đã được giải tán";
    public static String DISBAND_GROUP_CONTENT = "Toàn bộ thành viên sẽ rời khỏi nhóm, lịch sử trò chuyện cũng sẽ không còn tồn tại.";
    public static String KICK_BY_ADMIN_TITLE = "Bạn đã được mời ra khởi nhóm {groupName}";
    public static String KICK_BY_ADMIN_CONTENT = "Bạn sẽ không tìm và đọc nội dung tin nhắn vì bạn không còn trong nhóm nữa.";
    public static String NAME_GROUP_DEFAULT = "Widdy Trò chuyện nhóm";
    public static String NAME_GROUP_ADMIN = "Admin Widdy";
    public static String CONTENT_MESSAGE_ADMIN = "Chào bạn mới, bạn thấy Widdy như thế nào ?";
    public static String CONTENT_AUTO_ADMIN = "Xin chào, bạn cần hỗ trợ gì ạ ? Widdy ở đây luôn sẵn sàng hỗ trợ bạn.";
    private static List<List<String>> avatarGradient = Arrays.asList(
            Arrays.asList("#36d1dc", "#00c2e7", "#00b1ef", "#0c9def", "#5b86e5"),
            Arrays.asList("#cb356b", "#ca355c", "#c7374e", "#c33a40", "#bd3f32"),
            Arrays.asList("#43c6ac", "#76d6a8", "#a2e5a5", "#cef3a7", "#f8ffae"),
            Arrays.asList("#7389dc", "#009fdd", "#00adc4", "#46b5a2", "#86b787"),
            Arrays.asList("#42a9bd", "#40bfca", "#47d5d2", "#5bead6", "#78ffd6"),
            Arrays.asList("#f2994a", "#f4a549", "#f4b148", "#f4bd49", "#f2c94c"),
            Arrays.asList("#4ac29a", "#67d1b2", "#83e1c8", "#a0f0de", "#bdfff3"),
            Arrays.asList("#9542b4", "#a360bd", "#b17dc5", "#be98cd", "#cbb4d4"),
            Arrays.asList("#4b79a1", "#599bba", "#72bccf", "#94dee1", "#bdfff3"),
            Arrays.asList("#fd746c", "#fe7b6a", "#ff8269", "#ff8968", "#ff9068")

    );
    public static String AVATAR_GROUP_CHAT_ADMIN = "2021/07/30/product/28848603-c733-4031-a082-2d3e075dac38.png";

    public static String getNameWarningByType(String type) {
        switch (type) {
            case "jam":
                return "kẹt xe";
            case "danger":
                return "nguy hiểm";
            case "speed":
                return "tốc độ";
            case "accident":
                return "tai nạn";
            case "map_error":
                return "lỗi map";
            default:
                return "";

        }
    }

    public static User.Avatar genericAvatarDefault(String str) {
        int min = 0;
        int max = 9;
        String name = "";
        int random_int = (int) (Math.random() * (max - min + 1) + min);
        if (StringUtils.isNotEmpty(str) && !str.trim().equals("")) {
            String[] arraySplits = str.trim().split(" ");
            if (arraySplits.length > 1) {
                name = arraySplits[0].substring(0, 1) + arraySplits[arraySplits.length - 1].substring(0, 1);
            } else {
                name = arraySplits[0].substring(0, 1) + arraySplits[arraySplits.length - 1].substring(arraySplits[arraySplits.length - 1].length() - 1);
            }
        }
        return User.Avatar.builder()
                .name(name)
                .colors(avatarGradient.get(random_int))
                .build();
    }

    public static String removeUnicode(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        str = str.toLowerCase().trim();
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = temp.replaceAll("đ", "d");
        return pattern.matcher(temp).replaceAll("").trim();
    }

    public static String documentToJSON(Document doc) {
        JSONObject ret = new JSONObject();
        doc.forEach((prop, value) -> ret.put(prop, value));
        String json = new Gson().toJson(ret);
        return json;
    }

    public String replaceString(String str, Integer index, Integer length) {
        if (index > 0) {
            return str.substring(0, index) + "***" + str.substring(length + index);
        }
        return "***" + str.substring(length);
    }

    public String removeSensitiveWords(String content) {
        if (StringUtils.isNotEmpty(content)) {
            List<SensitiveWord> sensitiveWords = sensitiveWordRepository.getAll();
            String contentTemporary = content;
            for (SensitiveWord key : sensitiveWords) {
                String contentTo = contentTemporary.toLowerCase();
                while (contentTo.indexOf(key.getName()) != -1) {
                    contentTemporary = replaceString(contentTemporary, contentTo.indexOf(key.getName()), key.getName().length());
                    contentTo = contentTemporary.toLowerCase();
                }
            }
            return contentTemporary;
        }
        return content;
    }

    public static String getUserId(RoutingContext routingContext) {
        return routingContext.user().principal().getString("user_id");
    }

    public static MultiMap getParams(RoutingContext routingContext) {
        return routingContext.request().params();
    }

    public static String notificationByType(String type) {
        switch (type) {
            case "jam":
                return "Có một bình luận mới trong cảnh báo Kẹt xe của bạn";
            case "danger":
                return "Có một bình luận mới trong cảnh báo Nguy hiểm của bạn";
            case "accident":
                return "Có một bình luận mới trong cảnh báo Tai nạn của bạn";
            case "chat":
                return "Map chat: Bạn có tin nhắn mới";
            case "accept":
                return "Địa điểm {utility} của bạn đã được duyệt thành công. Cám ơn bạn vì đã đóng góp thông tin hữu ích cho Cộng đồng!";
            case "deny":
                return "Địa điểm {utility} của bạn đã bị từ chối vì không chính xác. Vui lòng liên hệ admin để được hỗ trợ!";
            case "order":
                return "Đơn hàng {order} của bạn đã được duyệt!";
            case "cancel":
                return "Đơn hàng {order} của bạn đã được hủy!";
        }
        return "Có một bình luận mới trong cảnh báo của bạn";
    }

    public static String notificationByTypeWarning(String type, Boolean isUserReporter) {
        switch (type) {
            case "jam":
                if (isUserReporter) return "{userName} đã bình luận vào cảnh báo Kẹt xe của bạn";
                return "{userName} cũng đã bình luận vào cảnh báo Kẹt xe";
            case "danger":
                if (isUserReporter) return "{userName} đã bình luận vào cảnh báo Nguy hiểm của bạn";
                return "{userName} cũng đã bình luận vào cảnh báo Nguy hiểm";
            case "accident":
                if (isUserReporter) return "{userName} đã bình luận vào cảnh báo Tai nạn của bạn";
                return "{userName} cũng đã bình luận vào cảnh báo Tai nạn";
        }
        return "Có một bình luận mới trong cảnh báo của bạn";
    }

    public static String CONTENT_NOTIFY_LIKE_COMMENT = "{userLike} thích bình luận của bạn";//tag
    public static String CONTENT_NOTIFY_REPLIED_COMMENT = "{userComment} đã trả lời bình luận của bạn";//tag
    public static String CONTENT_NOTIFY_COMMENT_USER = "{userComment} đã bình luận {object}";//tag
    public static String CONTENT_NOTIFY_COMMENT_TAG = "{userComment} đã bình luận một {object} mà bạn được gắn thẻ";//tag
    public static String CONTENT_NOTIFY_COMMENTED_CORONA = " đã bình luận địa điểm phong tỏa {address}";//tag
    public static String CONTENT_NOTIFY_COMMENTED_GROCERY_STORE = " đã bình luận {name}";//tag

    public static String contentCommentCorona(String type, String content) {
        if (StringType.HIDDEN.equals(type)) {
            return "Điểm phong tỏa " + content + ". Chúng tôi sẽ khóa tài khoản nếu tiếp tục gửi các bình luận không hợp lệ.";
        }
        return "Điểm phong tỏa " + content;
    }

    public static String titleCommentCorona(String type) {
        if (StringType.HIDDEN.equals(type)) {
            return "Bình luận bị từ chối!";
        }
        return "Bình luận đã duyệt thành công!";
    }

    public static String contentCommentGroceryStore(String type, String name) {
        if (StringType.HIDDEN.equals(type)) {
            return name + ". Chúng tôi sẽ khóa tài khoản nếu tiếp tục gửi các bình luận không hợp lệ.";
        }
        return name;
    }

    public static String titleCommentGroceryStore(String type) {
        if (StringType.HIDDEN.equals(type)) {
            return "Bình luận bị từ chối!";
        }
        return "Bình luận đã duyệt thành công!";
    }

    public static String notificationComment(String type, String name, long number) {
        String extra1 = number > 0?" và " + number + " người khác" :"";
        String extra2 = "bài viết";
        switch (type){
            case "avatar":
                extra2 = "về ảnh đại diện";
                break;
            case "cover":
                extra2 = "về ảnh bìa";
                break;
            case "image":
                extra2 = "ảnh";
                break;
        }
        return name + extra1 + " đã bình luận " + extra2 + " của bạn";
    }

    public static String notificationLike(String type, String name, long number) {
        String extra1 = number > 0?" và " + number + " người khác" :"";
        String extra2 = "bài viết";
        switch (type){
            case "avatar":
                extra2 = "ảnh đại diện";
                break;
            case "cover":
                extra2 = "ảnh bìa";
                break;
            case "image":
                extra2 = "ảnh";
                break;
        }
        return name + extra1 + " thích " + extra2 + " của bạn";
    }

    public static String contentOrderByStatus(String status) {
        if (status.equals("-1")) {
            return "Đơn hàng #{order} đã được hủy bởi nhà bán hàng";
        }
        if (status.equals("5")) {
            return "Kiện hàng [{partner_id}] của đơn hàng #{order} đã giao thành công. Cho chúng tối biết đánh giá của bạn về sản phẩm nhé";
        }
        if (status.equals("2") || status.equals("1")) {
            return "Đơn hàng #{order} đã được xác nhận";
        }
        if (status.equals("3") || status.equals("7") || status.equals("8") || status.equals("12")) {
            return "Kiện hàng [{partner_id}] của đơn hàng #{order} đã được người bán giao cho đơn vị vận chuyển";
        }
        if (status.equals("4") || status.equals("9") || status.equals("10")) {
            return "Kiện hàng [{partner_id}] của đơn hàng #{order} đang được giao";
        }
        return "Đơn hàng #{order} của bạn đang được xử lý";
    }

    public static String titleOrderByStatus(String status) {
        if (status.equals("-1")) {
            return "Đã hủy";
        }
        if (status.equals("5")) {
            return "Đã giao";
        }
        if (status.equals("2") || status.equals("1")) {
            return "Đã xác nhận";
        }
        if (status.equals("3") || status.equals("7") || status.equals("8") || status.equals("12")) {
            return "Chờ lấy hàng";
        }
        if (status.equals("4") || status.equals("9") || status.equals("10")) {
            return "Đang giao";
        }
        return "Đang xử lý";
    }
}
