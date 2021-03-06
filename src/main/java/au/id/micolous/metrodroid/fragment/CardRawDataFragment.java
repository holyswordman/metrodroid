package au.id.micolous.metrodroid.fragment;

import org.simpleframework.xml.Serializer;

import java.util.List;

import au.id.micolous.metrodroid.MetrodroidApplication;
import au.id.micolous.metrodroid.activity.AdvancedCardInfoActivity;
import au.id.micolous.metrodroid.card.Card;
import au.id.micolous.metrodroid.ui.ListItem;

public class CardRawDataFragment extends TreeListFragment {
    @Override
    protected List<ListItem> getItems() {
        Serializer serializer = MetrodroidApplication.getInstance().getSerializer();
        Card card = Card.fromXml(serializer, getArguments().getString(AdvancedCardInfoActivity.EXTRA_CARD));
        return card.getRawData();
    }
}
