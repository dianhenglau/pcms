package pcms.loginrecord;

import java.util.Locale;
import pcms.ContentView;
import pcms.RootView;
import pcms.Session;

/** LoginRecord controller. */
public final class LoginRecordController {
    /** Session. */
    private final Session session; // NOPMD - Temporary
    /** Login record repository. */
    private final LoginRecordRepository loginRecordRepository;

    /** Login record list view. */
    private final LoginRecordListView loginRecordListView;
    /** Root view. */
    private final RootView rootView;

    /** Construct. */
    public LoginRecordController(
            final Session session,
            final LoginRecordRepository loginRecordRepository,
            final LoginRecordListView loginRecordListView,
            final RootView rootView) {

        this.session = session;
        this.loginRecordRepository = loginRecordRepository;
        this.loginRecordListView = loginRecordListView;
        this.rootView = rootView;
    }

    /** Initialize. */
    public void init() {
        loginRecordListView.searchTf.addActionListener(e -> index(e.getActionCommand()));
    }

    /** List loginRecords. */
    public void index(final String search) {
        rootView.render(RootView.Views.MAIN_VIEW);
        rootView.mainView.contentView.render(ContentView.Views.LOGIN_RECORD_LIST);

        if (search.isEmpty()) {
            loginRecordListView.render(loginRecordRepository.all());
        } else {
            loginRecordListView.render(loginRecordRepository.filter(
                    x -> x.getUserId().toLowerCase(Locale.US)
                            .contains(search.toLowerCase(Locale.US))));
        }
    }
}
