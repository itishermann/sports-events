import LocalizedFormat from 'dayjs/plugin/localizedFormat';
import dayjs from 'dayjs';
import 'dayjs/locale/fr';

const setupDayJs = () => {
  dayjs.locale('fr');
  dayjs.extend(LocalizedFormat);
};

export default setupDayJs;
