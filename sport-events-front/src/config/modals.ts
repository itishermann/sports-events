import ActivityDetailsModal, {
  ActivityDetailsModalName,
} from '@/components/modals/ActivityDetailsModal';
import CreateActivityModal, { CreateActivityModalName } from '@/components/modals/CreateActivityModal';
import CreateSportModal, { CreateSportModalName } from '@/components/modals/CreateSportModal';
import UpdateSportModal, { UpdateSportModalName } from '@/components/modals/UpdateSportModal';
import UpdateUserModal, { UpdateUserModalName } from '@/components/modals/UpdateUserModal';

const modals = {
  [UpdateUserModalName]: UpdateUserModal,
  [CreateSportModalName]: CreateSportModal,
  [UpdateSportModalName]: UpdateSportModal,
  [CreateActivityModalName]: CreateActivityModal,
  [ActivityDetailsModalName]: ActivityDetailsModal,
};

declare module '@mantine/modals' {
  export interface MantineModalsOverride {
    modals: typeof modals;
  }
}

export default modals;
